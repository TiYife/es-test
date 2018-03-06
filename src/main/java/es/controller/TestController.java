package es.controller;

import es.Constant;
import es.entity.LawEntity;
import es.jpaRepository.DocRepository;
import es.service.DocService;
import es.service.LawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;

/**
 * Created by TYF on 2018/1/29.
 */
@RestController
public class TestController {

    @Autowired
    private LawService lawService;
    @Autowired
    private DocService docService;
    @Autowired
    private DocRepository docRepository;

    @RequestMapping("/add")
    @ResponseBody
    public String test(){
        LawEntity law = new LawEntity();
        law.setId(Constant.ID++);
        law.setTitle("just for testing");
        law.setContent("just for testing");
        return lawService.saveLaw(law);
    }

    @RequestMapping("/query")
    @ResponseBody
    public String query(@RequestParam String content){
        return lawService.searchLaw(null,null,content).toString();
    }

//    @RequestMapping("/record")
//    @ResponseBody
//    public String record(){
//        if(docService.recordDocs(new File(Constant.FILE_LOCATION)))return "success";
//        return "fail";
//    }
//
//    @RequestMapping("/read")
//    @ResponseBody
//    public String read(){
//        File file = new File(Constant.FILE_LOCATION+"数据库系统概论知识点整理.txt");
//        return docService.readDoc(file);
//    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(){
        File file = new File(Constant.FILE_LOCATION+"数据库系统概论知识点整理.txt");
        if (docService.saveDoc(file))return "success";
        return "false";
    }

    @RequestMapping("/saves")
    @ResponseBody
    public String saves(){
        File file = new File(Constant.FILE_LOCATION);
        if (docService.saveDocs(file))return "success";
        return "false";
    }
}
