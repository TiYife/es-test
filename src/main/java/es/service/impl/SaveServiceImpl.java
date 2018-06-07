package es.service.impl;

import es.Constant;
import es.Util.ConvertUtil;
import es.Util.FileUtil;
import es.entity.jpaEntity.TxtEntity;
import es.entity.jpaEntity.UpLogEntity;
import es.repository.esRepository.DocRepository;
import es.repository.jpaRepository.TxtRepository;
import es.repository.jpaRepository.UpLogRepository;
import es.service.SaveService;
import es.service.WordSeparateService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

import static es.Constant.dateFormat;
import static es.Constant.timeFormat;

/**
 * Created by TYF on 2018/2/26.
 */
@Service
public class SaveServiceImpl implements SaveService {

    /*日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    private TxtRepository txtRepository;
    @Autowired
    private DocRepository docRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private WordSeparateService wordSeparateService;
    @Autowired
    private UpLogRepository upLogRepository;

    private String originalDocLocation = Constant.originalDocLocation;
    private String xmlLocation = Constant.xmlLocation;
    private String newDocLocation = Constant.newDocLocation + Constant.dateFormat.format(new Date()) + "\\";
    private int upNo = 1;

    @Override
    public String uploadFile(MultipartFile multipartFile, int userId) {
        String suffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        if (!suffix.equals("txt") && !suffix.equals("zip"))
            return "wrong file type";
        UpLogEntity entity = new UpLogEntity();
        File file = FileUtil.uploadFile(multipartFile, newDocLocation);
        entity.setId(dateFormat.format(new Date()) + "-" + upNo++);
        entity.setFileName(multipartFile.getOriginalFilename());
        entity.setNewName(file.getName());
        entity.setUploader(userId);
        entity.setUpTime(timeFormat.format(new Date()));
        entity.setLocation(file.getAbsolutePath());
        entity.setIsSave(0);
        entity.setIsDel(0);
        upLogRepository.save(entity);
        return "success";
    }

    @Override
    @Async
    public String saveFile(UpLogEntity upLogEntity) {
        String rMsg;
        if(upLogEntity.getIsSave()!=0)return "该记录正在处理";
        upLogEntity.setIsSave(2);
        int userId = upLogEntity.getUploader();
        String upLogId = upLogEntity.getId();

        File up = new File(upLogEntity.getLocation());
        String suffix = FileUtil.getSuffix(up.getName());
        if (suffix.equals("txt")) {
            TxtEntity entity = recordTxt(upLogEntity.getLocation(),userId,upLogId);
            rMsg = saveTxt(entity);
            upLogEntity.setIsSave(1);
            upLogRepository.save(upLogEntity);
        } else if (suffix.equals("zip")) {
            File pack = deCompress(up);
            List<TxtEntity> list = recordTxts(pack, userId, upLogId);
            //rMsg = savePack(pack.getAbsolutePath());
            rMsg = saveTxts(list);
            FileUtil.delAllFile(pack.getAbsolutePath());
            upLogEntity.setIsSave(1);
            upLogRepository.save(upLogEntity);
        } else
            rMsg = "wrong file type";
        if(up.exists()){
            up.delete();
        }
        return rMsg;
    }

    @Override
    @Async
    public String saveAll() {
        List<UpLogEntity> list = upLogRepository.findByIsSave(0);//todo
        if(list.size()==0) {
            LOGGER.info("no new file to save");
            return "no new file to save";
        }
        for (UpLogEntity entity: list) {
            saveFile(entity);
        }
        return "success";
    }

    @Scheduled(cron = "0 30 0 * * ? ")
    public void setEveryDay() {
        saveAll();
        File file = new File(newDocLocation);
        FileUtil.delAllFile(file.getAbsolutePath());
        String newDirName = Constant.newDocLocation + Constant.dateFormat.format(new Date()) + "\\";
        file = new File(newDirName);
        file.mkdir();
        newDocLocation = newDirName;
        upNo = 1;
    }


    @Override
    public List<TxtEntity> listDocs() {
        return txtRepository.findAll();
    }

    @Override
    public String deleteTxt(String docId) {
        TxtEntity entity = txtRepository.findOne(docId);
        int status = entity.getStatus();
        if(status==1){
            docRepository.delete(docId);
        }
        if(status == 2){
            LOGGER.info(entity.getName()+" is saving, can't be deleted ");
            return "the file is saving, can't be deleted ";
        }

        File file = new File(entity.getLocation());
        File xml = new File(entity.getXmlLocation());
        if (file.exists()) file.delete();
        if(xml.exists()) xml.delete();

        txtRepository.delete(entity);
        return "success";
    }

    @Override
    public void deleteTxt(List<String> docIds) {
        for (String id : docIds
                ) {
            deleteTxt(id);
        }
    }

    @Override
    public List<UpLogEntity> listUpLog() {
        return upLogRepository.findAllOrderByUpTime();
    }

    @Override
    public String deleteUpLog(String id) {
        UpLogEntity entity = upLogRepository.findOne(id);
        int isSave = entity.getIsSave();
        if(isSave == 1){
            List<TxtEntity> list = txtRepository.findByUpLog(id);
            for (TxtEntity e :list
                 ) {
                deleteTxt(e.getId());
            }
        }
        if(isSave == 2){
            LOGGER.info(entity.getFileName()+" is saving, can't be deleted ");
            return "the file is saving, can't be deleted ";
        }
        if(isSave == -1){
            LOGGER.info(entity.getFileName()+" has been deleted, can't be deleted ");
            return "the file has been deleted, can't be deleted";
        }

        File file = new File(entity.getLocation());
        if (file.exists()) file.delete();
        upLogRepository.delete(id);
        return "success";
    }

    @Override
    public void deleteUpLog(List<String> docIds) {
        for (String id : docIds
                ) {
            deleteUpLog(id);
        }
    }

    //private String saveTxt(File up, String oldName, int userId) {
    private String saveTxt(TxtEntity entity) {
        File up = new File(entity.getLocation());
        String filePath;
        try {
            filePath = wordSeparateService.fileProcessAndSave(up.getAbsolutePath(), Constant.newDocLocation, xmlLocation);
        }catch (Exception e){
            return "分词处理错误";
        }
        if (filePath == null) {
            entity.setStatus(-2);
            return "word separate error";
        }

        File save = new File(filePath);
        //转移
        String saveLocation = FileUtil.fileMappingMove(up.getAbsolutePath(), newDocLocation, originalDocLocation);

        //索引
        docRepository.save(ConvertUtil.xmlToEntity(save));

        //记录
        entity.setStatus(1);
        entity.setSaveTime(timeFormat.format(new Date()));
        entity.setLocation(saveLocation);
        entity.setXmlLocation(filePath);
        txtRepository.save(entity);
        return "success";
    }

    private String saveTxts(List<TxtEntity> list) {
        File up;
        String filePath;
        List<HashMap<String,String>> xmlList = new ArrayList<>();
        HashMap<String,String> map = new HashMap<>();

        //分词+转移
        LOGGER.info("word separating ");
        int i = 1;
        for (TxtEntity entity: list) {
            i++;
            if(i%100==0)LOGGER.info("separating "+i);
            up = new File(entity.getLocation());
            try {
                filePath = wordSeparateService.fileProcessAndSave(up.getAbsolutePath(), Constant.newDocLocation, xmlLocation);
            }
            catch (Exception e){
                entity.setStatus(-2);
                continue;
            }
            if (filePath == null) {
                entity.setStatus(-2);
                continue;
            }

            //转移
            String saveLocation = FileUtil.fileMappingMove(up.getAbsolutePath(), Constant.newDocLocation, originalDocLocation);

            entity.setStatus(2);
            entity.setLocation(saveLocation);
            entity.setXmlLocation(filePath);
            txtRepository.save(entity);
        }

        //索引
        LOGGER.info("file indexing");
        indexXml(list);

        //记录
        for (TxtEntity entity: list) {
            entity.setStatus(1);
            entity.setSaveTime(timeFormat.format(new Date()));
            txtRepository.save(entity);
        }

        return "success";
    }

    private File deCompress(File pack) {
        LOGGER.info("file decompressing");
        File file;
        //解压
        try {
            String suffix = FileUtil.getSuffix(pack.getName());
            if (suffix.equals("zip"))
                file = FileUtil.unZip(pack, newDocLocation);
            else if (suffix.equals("rar"))
                file = FileUtil.unRar(pack, newDocLocation);
            else
                file = null;
        } catch (Exception e) {
            e.printStackTrace();
            file = null;
        }
        return file;
    }

    private void indexXml(List<TxtEntity> list) {
        if (!elasticsearchTemplate.indexExists(Constant.INDEX_NAME)) {
            elasticsearchTemplate.createIndex(Constant.INDEX_NAME);
        }

        int counter = 0;
        List queries = new ArrayList();
        for (TxtEntity entity : list) {
            counter++;
            String location = entity.getXmlLocation();
            if(location==null||location.equals(null)) continue;
            File fileToUp = new File(location);
            JSONObject json = ConvertUtil.xmlToJson(fileToUp);
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(entity.getId());
            indexQuery.setSource(String.valueOf(json));
            indexQuery.setIndexName(Constant.INDEX_NAME);
            indexQuery.setType("law");
            queries.add(indexQuery);
            //分批提交索引
            if (counter % 500 == 0) {
                elasticsearchTemplate.bulkIndex(queries);
                queries.clear();
                LOGGER.info("分组索引: " + counter);
            }
        }
        //不足批的索引
        if (queries.size() > 0) {
            elasticsearchTemplate.bulkIndex(queries);
        }
        elasticsearchTemplate.refresh(Constant.INDEX_NAME);
        LOGGER.info("索引完成");
    }

    private List<TxtEntity> recordTxts(File file, int userId, String upLogId) {
        //遍历
        LOGGER.info("list files");
       // File file = new File(newDocLocation + FileUtil.getFileName(dir.getName()));
        //File file = new File(dir);todo 为啥不直接dir
        List<String> fileList = new ArrayList<>();
        try {
            FileUtil.listFile(file, fileList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //记录
        LOGGER.info("file recording");
        int i = 0;
        List<TxtEntity> entityList = new ArrayList<>();
        for (String s : fileList
                ) {
            i++;
            if(i%100==0)
                LOGGER.info("recording "+i);
            entityList.add(recordTxt(s, userId, upLogId));
        }
        return entityList;
    }

    private TxtEntity recordTxt(String fileName, int userId , String upLogId) {
        File f= new File(fileName);
        TxtEntity entity = new TxtEntity();
        String uid = FileUtil.getFileUid(f.getAbsolutePath());
        entity.setId(uid);
        entity.setName(FileUtil.getFileName(f.getName()));
        entity.setUpLog(upLogId);
        entity.setLocation(f.getAbsolutePath());
        entity.setUploader(userId);
        entity.setUpTime(timeFormat.format(new Date()));
        entity.setStatus(0);
        txtRepository.save(entity);
        return entity;
    }


//    private String savePack(String pack) {
//        String save = wordSeparateService.multiFileProcessAndSave(pack, newDocLocation, xmlLocation);
//        if (save == null) return "word separate error";
//        FileUtil.dirMappingMove(pack, Constant.newDocLocation, originalDocLocation);
//
//        try {
//            saveXml(save);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "index error";
//        }
//        return "success";
//    }

//    private void saveXml(String fileLocation) throws IOException {
//        File file = new File(fileLocation);
//        List<String> list = new ArrayList<>();//TODO根据数据库中信息进行上传
//        FileUtil.listFile(file, list);//生成文件下文件位置的目录
//
//        indexXml(list);
//        TxtEntity txtEntity;
//        //将上传过的文档上传属性改为1
//        for (String docName : list
//                ) {
//            txtEntity = txtRepository.findOne(FileUtil.getFileName(docName));
//            if (txtEntity == null)
//                LOGGER.info(FileUtil.getFileName(docName) + "无上传记录");
//            else {
//                txtEntity.setIsSave(0);
//                txtEntity.setSaveTime(timeFormat.format(new Date()));
//                txtEntity.setXmlLocation(docName);
//                txtRepository.save(txtEntity);
//            }
//        }
//    }

}
