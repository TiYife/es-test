package es.service.impl;

import es.Constant;
import es.Util.ConvertUtil;
import es.Util.FileUtil;
import es.entity.jpaEntity.OriDocEntity;
import es.repository.esRepository.DocRepository;
import es.repository.jpaRepository.OriDocRepository;
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

    private String originalDocLocation=Constant.originalDocLocation;
    private String xmlLocation=Constant.xmlLocation;
    private String newDocLocation=Constant.newDocLocation+Constant.dateFormat.format(new Date())+"\\";

    public void uploadPackage(MultipartFile multipartFile, int userId){
        File file = FileUtil.uploadFile(multipartFile,newDocLocation);
        deCompress(file,userId);
    }

    @Async
    private boolean deCompress(File pack, int userId){
        //解压
        try {
            String suffix = FileUtil.getSuffix(pack.getName());
            if(suffix.equals("zip"))
                FileUtil.unZip(pack,newDocLocation);
            else if(suffix.equals("rar"))
                FileUtil.unRar(pack,newDocLocation);
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        pack.delete();

        //遍历
        File file = new File(newDocLocation+FileUtil.getFileName(pack.getName()));
        List<String> fileList = new ArrayList<>();
        try {
            FileUtil.listFile(file,fileList);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        //记录
        for (String s:fileList
                ) {
            recordOri(s,userId);
        }
        return true;
    }

    @Override
    public void uploadAndSave(MultipartFile multipartFile, int userId){
        File up = FileUtil.uploadFile(multipartFile,newDocLocation);
        //分词
        File save = wordSeparateService.fileProcessAndSave(up.getAbsolutePath(),Constant.newDocLocation,xmlLocation);

        //转移
        String saveLocation = FileUtil.fileMappingMove(up.getAbsolutePath(),newDocLocation,originalDocLocation);

        //索引
        docRepository.save(ConvertUtil.xmlToEntity(save));
        save.delete();

        //记录
        OriDocEntity entity = new OriDocEntity();
        entity.setId(FileUtil.getFileName(up.getName()));
        entity.setName(FileUtil.getFileName(multipartFile.getOriginalFilename()));
        entity.setLocation(saveLocation);
        entity.setUpTime(timeFormat.format(new Date()));
        entity.setUploader(userId);
        oriDocRepository.save(entity);
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

    @Override
    @Scheduled(cron = "0 30 0 * * ? ")
    //@Scheduled(cron = "0 0/5 * * * ? ")
    public void autoSave(){
        saveAllDoc();
        File file = new File(newDocLocation);
        file.delete();
        String newDirName = Constant.newDocLocation+Constant.dateFormat.format(new Date())+"\\";
        file = new File(newDirName);
        file.mkdir();
        newDocLocation=newDirName;
    }

    @Override
    public List<OriDocEntity> listDocs(){
        return oriDocRepository.findAll();//todo
    }
    @Override
    public void deleteDoc(String docId){
        File file = new File(oriDocRepository.findOne(docId).getLocation());
        if(file.exists()) file.delete();
        oriDocRepository.delete(docId);
        docRepository.delete(docId);
    }
    @Override
    public void delete(List<String> docIds){
        File file;
        for (String id:docIds
             ) {
            file = new File(oriDocRepository.findOne(id).getLocation());
            if(file.exists()) file.delete();
            oriDocRepository.delete(id);
            docRepository.delete(id);
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
                oriDocEntity.setSave(true);
                oriDocEntity.setSaveTime(timeFormat.format(new Date()));
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
