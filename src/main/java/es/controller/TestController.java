package es.controller;

import es.Constant;
import es.entity.DocEntity;
import es.jpaRepository.XmlRepository;
import es.service.SaveService;
import es.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;

/**
 * Created by TYF on 2018/1/29.
 */
@RestController
public class TestController {

    @Autowired
    private SearchService searchService;
    @Autowired
    private SaveService saveService;
    @Autowired
    private XmlRepository xmlRepository;

//    @RequestMapping("/add")
//    @ResponseBody
//    public String test(){
//        DocEntity law = new DocEntity();
//        law.setContent("just for testing");
//        return searchService.saveLaw(law);
//    }
//
//    @RequestMapping("/query")
//    @ResponseBody
//    public String query(@RequestParam String content){
//        return searchService.searchLaw(null,null,content).toString();
//    }
//
////    @RequestMapping("/record")
////    @ResponseBody
////    public String record(){
////        if(saveService.recordDocs(new File(Constant.FILE_LOCATION)))return "success";
////        return "fail";
////    }
////
////    @RequestMapping("/read")
////    @ResponseBody
////    public String read(){
////        File file = new File(Constant.FILE_LOCATION+"数据库系统概论知识点整理.txt");
////        return saveService.readDoc(file);
////    }
//
//    @RequestMapping("/save")
//    @ResponseBody
//    public String save(){
//        File file = new File(Constant.FILE_LOCATION+"20140107\\安徽\\安徽省安庆市中级人民法院\\行政案件\\bbf10813-8274-48e7-b6af-56dc1b4a29c5.xml");
//        if (saveService.saveDoc(file))return "success";
//        return "false";
//    }
//
//    @RequestMapping("/saves")
//    @ResponseBody
//    public String saves(){
//        File file = new File(Constant.FILE_LOCATION);
//        if (saveService.saveDocs(file))return "success";
//        return "false";
//    }
}
