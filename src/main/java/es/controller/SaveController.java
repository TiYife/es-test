package es.controller;

import com.google.gson.Gson;
import es.Util.FileUtil;
import es.Util.IdentityUtil;
import es.entity.jpaEntity.UpLogEntity;
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
    public String upload(@RequestParam("file")MultipartFile file, HttpServletRequest request){
        String uId=IdentityUtil.getCookieValue(request,"userId");
        if(uId==null)    return "not login";

        int userId = Integer.parseInt(uId);
        String pwd = IdentityUtil.getCookieValue(request,"userPasswd");
        if(!userRepository.findById(userId).getPassword().equals(pwd))  return "not login";

        String message = saveService.uploadFile(file,userId);
        if(message.equals("success"))
            return "{}";
        else return "{error:"+message+"}";
    }

    @RequestMapping("/list-uploaded")
    @ResponseBody
    public String listUploaded(){
        return new Gson().toJson(saveService.listUploaded());
    }

    @RequestMapping("/list-uploading")
    @ResponseBody
    public String listUploading(){
        return new Gson().toJson(saveService.listUploading());
    }

    @RequestMapping("/save-file")
    @ResponseBody
    public String saveFile(String id){
        UpLogEntity entity = upLogRepository.findOne(id);
        return saveService.saveFile(entity);
    }

    @RequestMapping("/delete-up")
    @ResponseBody
    public String deleteUp(String id){
        saveService.deleteUpLog(id);
        //刷新
        return "success";
    }

    @RequestMapping("/list-docs")
    @ResponseBody
    public String listDocs(){
        return new Gson().toJson(saveService.listDocs());
    }

    @RequestMapping("/delete-doc")
    @ResponseBody
    public String delete(String id){
        saveService.deleteDoc(id);
        //刷新
        return "success";
    }

    @RequestMapping("/save-all")
    @ResponseBody
    public String saveAll(){
        saveService.saveAllDoc();
        return "success";
    }
}
