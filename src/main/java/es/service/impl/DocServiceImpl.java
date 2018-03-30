package es.service.impl;

import com.google.gson.Gson;
import es.Constant;
import es.entity.DocumentsEntity;
import es.entity.LawEntity;
import es.esRepository.LawRepository;
import es.jpaRepository.DocRepository;
import es.service.DocService;
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
public class DocServiceImpl implements DocService{

    @Autowired
    private LawRepository lawRepository;
    @Autowired
    private DocRepository docRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public boolean saveDoc(File file){
        try {
            LawEntity lawEntity = new LawEntity();
            lawEntity.setId(Constant.ID++);
            lawEntity.setTitle(file.getName());
            lawEntity.setContent(readDoc(file));
            lawRepository.save(lawEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean saveDocs(File file){
        recordDocs(file);
        int counter = 0;
        List<LawEntity> list = createListToUp();
        try {
            if (!elasticsearchTemplate.indexExists(Constant.INDEX_NAME)) {
                elasticsearchTemplate.createIndex(Constant.INDEX_NAME);
            }
            Gson gson = new Gson();
            List queries = new ArrayList();
            for (LawEntity law : list) {
                counter++;
                IndexQuery indexQuery = new IndexQuery();
                indexQuery.setId(law.getId().toString());
                indexQuery.setSource(gson.toJson(law));
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

    //将所有文件信息记录到数据库，以便之后根据数据库进行索引
    private boolean recordDocs(File file) {
        try {
            List<DocumentsEntity> list = new ArrayList<DocumentsEntity>();
            if(!listDocs(file,list))
                return false;
            docRepository.save(list);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //列举出文件夹下所有的文件
    private boolean listDocs(File file , List<DocumentsEntity> list) throws IOException {
        if(!file.exists())return false;
        if(file.isDirectory()) {
            System.out.print("读取文件夹:");
            System.out.println(file.getAbsolutePath());
            File[] fileList = file.listFiles();
            for (File sub:fileList
                    ) {
                listDocs(sub,list);
            }
        }else{
            String title=file.getAbsolutePath();
            System.out.print("读取文件:");
            System.out.println(title);
            DocumentsEntity documentsEntity=new DocumentsEntity();
            documentsEntity.setTitle(title);
            list.add(documentsEntity);
        }
        return true;
    }

    //读取文件的信息并返回String
    private String readDoc(File file) {
        String encoding = Constant.ENCODING;
        Long length = file.length();
        byte[] content = new byte[length.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(content);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(content, encoding);
        } catch (UnsupportedEncodingException e) {
            System.out.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    //将还未索引的文件创建成list便于上传
    private List<LawEntity> createListToUp(){
        List<LawEntity> lawEntities=new ArrayList<LawEntity>();
        LawEntity lawEntity;
        List<DocumentsEntity> documentsEntities=docRepository.findAllByUploadedAndAndDeleted(false,false);
        for (DocumentsEntity documentsEntity:documentsEntities
             ) {
            lawEntity=new LawEntity();
            lawEntity.setId(Constant.ID++);
            File file=new File(documentsEntity.getTitle());
            lawEntity.setTitle(getFileName(file.getName()));
            lawEntity.setContent(readDoc(file));
            lawEntities.add(lawEntity);
        }
        return lawEntities;
    }

    //文件名去后缀
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
