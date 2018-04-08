package es.service.impl;

import es.Constant;
import es.Util.ConvertUtil;
import es.entity.jpaEntity.XmlEntity;
import es.entity.esEntity.DocEntity;
import es.repository.esRepository.DocRepository;
import es.repository.jpaRepository.XmlRepository;
import es.service.SaveService;
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
    private XmlRepository xmlRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

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
    public boolean saveDocs(File file){
        int counter = 0;
        recordXml(file);//更新数据库，将未索引的数据存入数据库
        List<String> list = listNewXml();//找到数据库中未索引的数据
        try {
            if (!elasticsearchTemplate.indexExists(Constant.INDEX_NAME)) {
                elasticsearchTemplate.createIndex(Constant.INDEX_NAME);
            }
            List queries = new ArrayList();
            for (String fileLocation : list) {
                counter++;
                File fileToUp = new File(fileLocation);
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
        } catch (Exception e) {
            LOGGER.info("索引异常： " + e.getMessage());
            return false;
        }
        return true;
    }

    //从数据库中找到还未索引的文件，将他们的路径创建成list，便于上传
    private List<String> listNewXml(){
        List<String> list =new ArrayList<String>();
        List<XmlEntity> xmlEntities= xmlRepository.findByUpAndDel(false,false);
        for (XmlEntity xmlEntity:xmlEntities
                ) {
            list.add(xmlEntity.getLocation());
        }
        return list;
    }

    //将所有文件信息记录到数据库，以便之后根据数据库进行索引
    private boolean recordXml(File file) {
        try {
            List<XmlEntity> list = new ArrayList<XmlEntity>();
            if(!listXml(file,list))
                return false;
            xmlRepository.save(list);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //列举出文件夹下所有的文件,生成list，稍后存入数据库
    private boolean listXml(File file , List<XmlEntity> list) throws IOException {
        if(!file.exists())return false;
        if(file.isDirectory()) {
            LOGGER.info("读取文件夹:"+file.getAbsolutePath());
            File[] fileList = file.listFiles();
            for (File sub:fileList
                    ) {
                listXml(sub,list);
            }
        }else{
            String location=file.getAbsolutePath();
            LOGGER.info("读取文件:"+location);
            XmlEntity xmlEntity=new XmlEntity();
            xmlEntity.setId(getFileName(file.getName()));
            xmlEntity.setLocation(location);
            xmlEntity.setUp(false);
            xmlEntity.setDel(false);
            list.add(xmlEntity);
        }
        return true;
    }

    private String getFileName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
}
