package es.controller;

import com.google.gson.Gson;
import es.Util.FileUtil;
import es.entity.jpaEntity.UserEntity;
import es.service.SaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by TYF on 2018/5/14.
 */
@Controller
public class SaveController {

    @Autowired
    SaveService saveService;

    @RequestMapping("/doc-admin")
    public String docAdmin(){
        return "doc-admin";
    }

    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("files")List<MultipartFile> files, HttpSession session){
        UserEntity userEntity = (UserEntity)session.getAttribute("user");
        if(userEntity.equals(null))
            return "redirect:login";
        if(saveService.uploadDoc(files,userEntity))
            return "success";
        else
            return "fail";
    }

    @RequestMapping("/list-docs")
    @ResponseBody
    public String listDocs(){
        return new Gson().toJson(saveService.listDocs());
    }

    @RequestMapping("delete")
    @ResponseBody
    public String delete(String id){
        saveService.deleteDoc(id);
        return "success";
    }
}
