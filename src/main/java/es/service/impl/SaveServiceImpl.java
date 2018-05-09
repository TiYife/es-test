package es.service.impl;

import es.Constant;
import es.Util.ConvertUtil;
import es.Util.FileUtil;
import es.entity.esEntity.DocEntity;
import es.entity.jpaEntity.OriDocEntity;
import es.repository.esRepository.DocRepository;
import es.repository.jpaRepository.OriDocRepository;
import es.repository.jpaRepository.XmlRepository;
import es.service.SaveService;
import es.service.WordSeparateService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

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
    private DocRepository docRepository;
    @Autowired
    private OriDocRepository oriDocRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private WordSeparateService wordSeparateService;

    private String originalDocLocation=Constant.originalDocLocation;
    private String xmlLocation=Constant.xmlLocation;
    private String newDocLocation=Constant.newDocLocation;

    public String uploadDoc(){
        return null;
    }

    public String uploadDocs(){
        return null;
    }

    @Override
    public String saveNewDocs(){
        try {
            wordSeparateService.multiFileProcessAndSave(newDocLocation,newDocLocation,xmlLocation);
            FileUtil.dirMappingMove(newDocLocation,newDocLocation,originalDocLocation);
            saveDocs(xmlLocation);
            FileUtil.delAllFile(xmlLocation);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "success";
    }





    @Override
    public boolean saveDoc(File file){
        try {
            DocEntity docEntity = ConvertUtil.xmlToEntity(file);
            docRepository.save(docEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void saveDocs(String fileLocation) throws IOException, JSONException {
        File file = new File(fileLocation);
        List<String> list = new ArrayList<>() ;
        FileUtil.listFile(file,list);//生成文件下文件位置的目录

        saveDocs(list);

        //将上传过的文档上传属性改为1
        for (String docName: list
             ) {
            oriDocRepository.findOne(FileUtil.getFileName(docName)).setSave(true);
        }
    }

    private void saveDocs(List<String> list) throws JSONException {
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
            indexQuery.setId(json.get("docId").toString());
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
