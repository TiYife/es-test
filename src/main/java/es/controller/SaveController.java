package es.controller;

import com.google.gson.Gson;
import es.Util.IdentityUtil;
import es.entity.jpaEntity.UpLogEntity;
import es.entity.jpaEntity.UserEntity;
import es.repository.jpaRepository.UpLogRepository;
import es.repository.jpaRepository.UserRepository;
import es.service.SaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by TYF on 2018/5/14.
 */
@Controller
public class SaveController {

    @Autowired
    SaveService saveService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UpLogRepository upLogRepository;

    @RequestMapping("/doc-admin")
    public String docAdmin(){
        return "doc-admin";
    }

    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file")MultipartFile file, HttpSession session){
        String message;
        UserEntity user = (UserEntity) session.getAttribute("user");
        if(user==null)
            message = "你还没有登录";
        else if(user.getRole()!= 1)
            message = "你没有进行该操作的权限";
        else {
        int userId = user.getId();
        message = saveService.uploadFile(file,userId);
        }
        if(message.equals("success"))
            return "{}";
        else return "{error:"+message+"}";
    }

    @RequestMapping("/list-up")
    @ResponseBody
    public String listUploading(){
        return new Gson().toJson(saveService.listUpLog());
    }

    @RequestMapping("/save-up")
    @ResponseBody
    public String saveFile(String id){
        UpLogEntity entity = upLogRepository.findOne(id);
        return saveService.saveFile(entity);
    }

    @RequestMapping("/delete-up")
    @ResponseBody
    public String deleteUp(String id){
        return saveService.deleteUpLog(id);
    }

    @RequestMapping("/list-docs")
    @ResponseBody
    public String listDocs(){
        return new Gson().toJson(saveService.listDocs());
    }

    @RequestMapping("/delete-doc")
    @ResponseBody
    public String delete(String id){
        saveService.deleteTxt(id);
        //刷新
        return "success";
    }

    @RequestMapping("/save-all")
    @ResponseBody
    public String saveAll(){
        saveService.saveAll();
        return "success";
    }
}
