package es.service.impl;

import es.Constant;
import es.Util.ConvertUtil;
import es.Util.FileUtil;
import es.entity.esEntity.DocEntity;
import es.entity.jpaEntity.OriDocEntity;
import es.entity.jpaEntity.UserEntity;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private WordSeparateService wordSeparateService;

    private String originalDocLocation=Constant.originalDocLocation;
    private String xmlLocation=Constant.xmlLocation;
    private String newDocLocation=Constant.newDocLocation;


    @Override
    public boolean uploadDoc(MultipartFile multipartFile, UserEntity userEntity){
        try {
            OriDocEntity entity = FileUtil.uploadFile(multipartFile,newDocLocation,userEntity.getId());
            oriDocRepository.save(entity);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean uploadDoc(List<MultipartFile> multipartFileList,UserEntity userEntity){
        try {
            List<OriDocEntity> entities = FileUtil.uploadFile(multipartFileList,newDocLocation,userEntity.getId());
            oriDocRepository.save(entities);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean saveDoc(MultipartFile multipartFile, UserEntity userEntity){
        if(uploadDoc(multipartFile,userEntity)){
            if(saveDoc())
                return true;
        }
        return false;
    }

    @Override
    public boolean saveDoc(){
        File file = new File(newDocLocation);
        if(!file.exists()){
            LOGGER.info("没有新文本");
            return false;
        }
        try {
            wordSeparateService.multiFileProcessAndSave(newDocLocation,newDocLocation,xmlLocation);
            FileUtil.dirMappingMove(newDocLocation,newDocLocation,originalDocLocation);
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
    public boolean saveDoc(List<MultipartFile> multipartFileList,UserEntity userEntity){
       if(uploadDoc(multipartFileList,userEntity)){
           if(saveDoc())
               return true;
       }
       return false;
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
            oriDocEntity.setSave(true);
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

    //从数据库中找到还未索引的文件，将他们的路径创建成list，便于上传
//    private List<String> listNewXml(){
//        List<String> list =new ArrayList<String>();
//        List<XmlEntity> xmlEntities= xmlRepository.findByUpAndDel(false,false);
//        for (XmlEntity xmlEntity:xmlEntities
//                ) {
//            list.add(xmlEntity.getLocation());
//        }
//        return list;
//    }

//    //将所有文件信息记录到数据库，返回文件的list
//    private String recordFile(String fileLocation) throws IOException {
//        File file = new File(fileLocation);
//        List<String> list = new ArrayList<String>();
//        if(!listFile(file,list)){
//            OriDocEntity entity = new OriDocEntity();
//            File f;
//            for (String location: list
//                 ) {
//                f = new File(location);
//            }
//        }
//
//    }


}
