package es.controller;

import es.Util.FileUtil;
import es.entity.jpaEntity.UserEntity;
import es.service.SaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by TYF on 2018/5/14.
 */
@Controller
public class SaveController {

    @Autowired
    SaveService saveService;

    @RequestMapping("/up")
    public String up(){
     return "upload";
    }

    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("files")List<MultipartFile> files){
        UserEntity userEntity =new UserEntity();
        userEntity.setId(1);
        if(saveService.saveDoc(files,userEntity))
            return "success";
        else
            return "fail";
    }
}
