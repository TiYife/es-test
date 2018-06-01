package es.controller;

import com.google.gson.Gson;
import es.Util.FileUtil;
import es.Util.IdentityUtil;
import es.entity.jpaEntity.UserEntity;
import es.repository.jpaRepository.UserRepository;
import es.service.SaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by TYF on 2018/5/14.
 */
@Controller
public class SaveController {

    @Autowired
    SaveService saveService;
    @Autowired
    UserRepository userRepository;

    @RequestMapping("/doc-admin")
    public String docAdmin(){
        return "doc-admin";
    }

    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("files")MultipartFile file, HttpServletRequest request){
        String uId=IdentityUtil.getCookieValue(request,"userId");
        if(uId==null)    return "not login";

        int userId = Integer.parseInt(uId);
        String pwd = IdentityUtil.getCookieValue(request,"userPasswd");
        if(!userRepository.findById(userId).getPassword().equals(pwd))  return "not login";
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        if (suffix.equals("txt"))
            saveService.uploadAndSave(file,userId);
        else if(suffix.equals("rar"))
            saveService.uploadPackage(file,userId);
        else if(suffix.equals("zip"))
            saveService.uploadPackage(file,userId);
        else
            return "wrong file type";
        return "success";
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
        //刷新
        return "success";
    }

    @RequestMapping("save-all")
    @ResponseBody
    public String saveAll(){
        saveService.autoSave();
        return "success";
    }
}
