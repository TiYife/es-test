package es.service.impl;

import es.Constant;
import es.Util.ConvertUtil;
import es.Util.FileUtil;
import es.entity.jpaEntity.OriDocEntity;
import es.entity.jpaEntity.UpLogEntity;
import es.repository.esRepository.DocRepository;
import es.repository.jpaRepository.OriDocRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    private OriDocRepository oriDocRepository;
    @Autowired
    private DocRepository docRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private WordSeparateService wordSeparateService;
    @Autowired
    private UpLogRepository upLogRepository;

    private String originalDocLocation=Constant.originalDocLocation;
    private String xmlLocation=Constant.xmlLocation;
    private String newDocLocation=Constant.newDocLocation+Constant.dateFormat.format(new Date())+"\\";
    private int upNo=1;

    byte flagT=1,flagF=0;

    @Override
    public String uploadFile(MultipartFile multipartFile, int userId){
        String suffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        if(!suffix.equals("txt") && !suffix.equals("rar") && !suffix.equals("zip"))
            return "wrong file type";
        UpLogEntity entity = new UpLogEntity();
        File file = FileUtil.uploadFile(multipartFile,newDocLocation);
        entity.setId(dateFormat.format(new Date())+ "-"+upNo++);
        entity.setFileName(multipartFile.getOriginalFilename());
        entity.setNewName(file.getName());
        entity.setUploader(userId);
        entity.setUpTime(timeFormat.format(new Date()));
        entity.setLocation(file.getAbsolutePath());
        entity.setIsSave(flagF);
        entity.setIsDel(flagF);
        upLogRepository.save(entity);
        return "success";
    }

    @Override
    public String saveFile(UpLogEntity upLogEntity){
        File up = new File(upLogEntity.getLocation());
        int userId = upLogEntity.getUploader();
        String oldName = upLogEntity.getFileName();
        String suffix = FileUtil.getSuffix(up.getName());
        if(suffix.equals("txt")){
            return saveTxt(up,oldName,userId);
        }
        else if(suffix.equals("rar")||suffix.equals("zip")){
            File pack = deCompress(up);
            recordOris(pack,userId);
            return savePack(pack.getAbsolutePath());
        }
        else return "wrong type";
    }

    @Override
    public boolean saveAllDoc(){
        File file = new File(newDocLocation);
        if(!file.exists()){
            LOGGER.info("没有新文本");
            return false;
        }
        try {
            wordSeparateService.multiFileProcessAndSave(newDocLocation,newDocLocation,xmlLocation);
            FileUtil.dirMappingMove(newDocLocation,Constant.newDocLocation,originalDocLocation);
            saveXml(xmlLocation);
            FileUtil.delAllFile(xmlLocation);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
            return false;
        }
        return true;
    }

    @Scheduled(cron = "0 30 0 * * ? ")
    public void setEveryDay(){
        saveAllDoc();
        File file = new File(newDocLocation);
        file.delete();
        String newDirName = Constant.newDocLocation+Constant.dateFormat.format(new Date())+"\\";
        file = new File(newDirName);
        file.mkdir();
        newDocLocation=newDirName;
        upNo=1;
    }



    @Override
    public List<OriDocEntity> listDocs(){
        return oriDocRepository.findAll();
    }
    @Override
    public void deleteDoc(String docId){
        OriDocEntity entity = oriDocRepository.findOne(docId);
        File file = new File(entity.getLocation());
        if(file.exists()) file.delete();
        oriDocRepository.delete(docId);
        if(entity.getIsSave().equals(flagT))
            docRepository.delete(docId);
    }
    @Override
    public void deleteDoc(List<String> docIds){
        for (String id:docIds
                ) {
            deleteDoc(id);
        }
    }

    @Override
    public List<UpLogEntity> listUploading(){
       return upLogRepository.findByIsSaveOrderByUpTime(flagF);
    }

    @Override
    public List<UpLogEntity> listUploaded(){
        return upLogRepository.findByIsSaveOrderByUpTime(flagT);
    }

    @Override
    public void deleteUpLog(String id){
        UpLogEntity entity = upLogRepository.findOne(id);
        if(entity.getIsSave().equals(flagT))return;
        File file = new File(entity.getLocation());
        if(file.exists()) file.delete();
        upLogRepository.delete(id);
    }

    @Override
    public void deleteUpLog(List<String> docIds){
        for (String id:docIds
                ) {
            deleteUpLog(id);
        }
    }



    private String saveTxt(File up, String oldName, int userId){
        String  filePath = wordSeparateService.fileProcessAndSave(up.getAbsolutePath(),Constant.newDocLocation,xmlLocation);
        if(filePath==null) return "word separate error";

        File save = new File(filePath);
        //转移
        String saveLocation = FileUtil.fileMappingMove(up.getAbsolutePath(),newDocLocation,originalDocLocation);

        //索引
        docRepository.save(ConvertUtil.xmlToEntity(save));

        //记录
        OriDocEntity entity = new OriDocEntity();
        entity.setId(FileUtil.getFileName(up.getName()));
        entity.setName(FileUtil.getFileName(oldName));
        entity.setLocation(saveLocation);
        entity.setUpTime(timeFormat.format(new Date()));
        entity.setUploader(userId);
        oriDocRepository.save(entity);
        return "success";
    }

    private String savePack(String pack){
        String save = wordSeparateService.multiFileProcessAndSave(pack,newDocLocation,xmlLocation);
        if(save==null) return "word separate error";

        FileUtil.dirMappingMove(pack,Constant.newDocLocation,originalDocLocation);

        try {
            saveXml(save);
        } catch (IOException e) {
            e.printStackTrace();
            return "index error";
        }
        return "success";
    }

    @Async
    private File deCompress(File pack) {
        //解压
        try {
            String suffix = FileUtil.getSuffix(pack.getName());
            if (suffix.equals("zip"))
                return FileUtil.unZip(pack, newDocLocation);
            else if (suffix.equals("rar"))
                return FileUtil.unRar(pack, newDocLocation);
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveXml(String fileLocation) throws IOException{
        File file = new File(fileLocation);
        List<String> list = new ArrayList<>() ;
        FileUtil.listFile(file,list);//生成文件下文件位置的目录

        indexXml(list);
        OriDocEntity oriDocEntity;
        //将上传过的文档上传属性改为1
        for (String docName: list
             ) {
            oriDocEntity=oriDocRepository.findOne(FileUtil.getFileName(docName));
            if(oriDocEntity==null)
                LOGGER.info(FileUtil.getFileName(docName)+"无上传记录");
            else {

                oriDocEntity.setIsSave(flagT);
                oriDocEntity.setSaveTime(timeFormat.format(new Date()));
                oriDocEntity.setXmlLocation(docName);
                oriDocRepository.save(oriDocEntity);
            }
        }
    }

    private void indexXml(List<String> list){
        if (!elasticsearchTemplate.indexExists(Constant.INDEX_NAME)) {
            elasticsearchTemplate.createIndex(Constant.INDEX_NAME);
        }

        int counter = 0;
        List queries = new ArrayList();
        for (String fileLoc : list) {
            counter++;
            File fileToUp = new File(fileLoc);
            JSONObject json = ConvertUtil.xmlToJson(fileToUp);
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(FileUtil.getFileName(fileLoc));
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

    private void recordOris(File dir,int userId){
        //遍历
        File file = new File(newDocLocation+FileUtil.getFileName(dir.getName()));
        List<String> fileList = new ArrayList<>();
        try {
            FileUtil.listFile(file,fileList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //记录
        for (String s:fileList
                ) {
            recordOri(s,userId);
        }
    }

    private void recordOri(String s, int userId){
        File f ;
        OriDocEntity entity;
        f = new File(s);
        entity = new OriDocEntity();
        entity.setName(FileUtil.getFileName(f.getName()));
        String uuid = UUID.randomUUID().toString();
        uuid= uuid.replaceAll("-", "");
        entity.setId(uuid);
        FileUtil.reName(f,uuid);
        entity.setUploader(userId);
        entity.setUpTime(timeFormat.format(new Date()));
        entity.setLocation(f.getAbsolutePath());
        oriDocRepository.save(entity);
    }

}
