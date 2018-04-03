package es;


import es.entity.DocEntity;
import es.service.SaveService;
import es.service.SearchService;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 13051 on 2018/2/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TEST {

    @Autowired
    SaveService saveService;
    @Autowired
    SearchService searchService;

    @Test
    public void testXml() {

    }

    @Test
    public void testSave(){
        File file = new File(Constant.FILE_LOCATION+"20140107\\安徽\\安徽省安庆市中级人民法院\\行政案件\\bbf10813-8274-48e7-b6af-56dc1b4a29c5.xml");
        saveService.saveDoc(file);
    }

    @Test
    public void testSaveDocs(){
        File file = new File(Constant.FILE_LOCATION+"20140107\\");
        saveService.saveDocs(file);
    }

    @Test
    public void testSearch(){
        List<DocEntity> docEntities=searchService.searchLaw(1,10,"content","被告人");
    }
}

