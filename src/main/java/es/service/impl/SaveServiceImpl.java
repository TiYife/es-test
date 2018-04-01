package es.service.impl;

import com.google.gson.Gson;
import es.Constant;
import es.Util.ConvertUtil;
import es.entity.XmlEntity;
import es.entity.DocEntity;
import es.esRepository.LawRepository;
import es.jpaRepository.XmlRepository;
import es.service.SaveService;
import org.json.JSONObject;
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

    @Autowired
    private LawRepository lawRepository;
    @Autowired
    private XmlRepository xmlRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public boolean saveDoc(File file){
        try {
            DocEntity docEntity = ConvertUtil.xmlToEntity(file);
            lawRepository.save(docEntity);
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
            Gson gson = new Gson();
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
                    System.out.println("bulkIndex counter : " + counter);
                }
            }
            //不足批的索引
            if (queries.size() > 0) {
                elasticsearchTemplate.bulkIndex(queries);
            }
            elasticsearchTemplate.refresh(Constant.INDEX_NAME);
            System.out.println("bulkIndex completed.");
        } catch (Exception e) {
            System.out.println("IndexerService.bulkIndex e;" + e.getMessage());
            return false;
        }
        return true;
    }

    //从数据库中找到还未索引的文件，将他们的路径创建成list，便于上传
    private List<String> listNewXml(){
        List<String> list =new ArrayList<String>();
        List<XmlEntity> xmlEntities= xmlRepository.findAllByUploadAndAndDelete(false,false);
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
            System.out.print("读取文件夹:");
            System.out.println(file.getAbsolutePath());
            File[] fileList = file.listFiles();
            for (File sub:fileList
                    ) {
                listXml(sub,list);
            }
        }else{
            String location=file.getAbsolutePath();
            System.out.print("读取文件:");
            System.out.println(location);
            XmlEntity xmlEntity=new XmlEntity();
            xmlEntity.setId(file.getName());
            xmlEntity.setLocation(location);
            list.add(xmlEntity);
        }
        return true;
    }

    //读取文件的信息并返回String
//    private String readDoc(File file) {
//        String encoding = Constant.ENCODING;
//        Long length = file.length();
//        byte[] content = new byte[length.intValue()];
//        try {
//            FileInputStream in = new FileInputStream(file);
//            in.read(content);
//            in.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            return new String(content, encoding);
//        } catch (UnsupportedEncodingException e) {
//            System.out.println("The OS does not support " + encoding);
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    //文件名去后缀
//    private String getFileName(String filename) {
//        if ((filename != null) && (filename.length() > 0)) {
//            int dot = filename.lastIndexOf('.');
//            if ((dot >-1) && (dot < (filename.length()))) {
//                return filename.substring(0, dot);
//            }
//        }
//        return filename;
//    }
}
