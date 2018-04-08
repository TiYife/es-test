package es.controller;

import es.entity.UserEntity;
import es.jpaRepository.UserRepository;
import es.service.impl.WordSeparateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by TYF on 2018/1/29.
 */
@Controller
public class UserController {


    @Autowired
    public UserRepository userRepository;
    private WordSeparateServiceImpl wordSeparateService = new WordSeparateServiceImpl();

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

    @RequestMapping("/index")
    public String index(HttpServletRequest request, String name){
        request.setAttribute("text","hhh");
        String res="";
        if(name!=null)
            res=wordSeparateService.getnn(name);
        request.setAttribute("re",res);
        return "test_page";
    }

    @RequestMapping("/")
    public String toIndex(){
        return "index";
    }

    @RequestMapping("/admin")
    public String toAdmin(){
        return "admin";
    }
    @RequestMapping("/advanced-search")
    public String toAdvancedSearch(){
        return "advanced-search";
    }
    @RequestMapping("/favorites")
    public String toFavorites(){
        return "favorites";
    }

    @RequestMapping("/login")
    @ResponseBody
    public  String login(@RequestParam("userId") int userId,
                         @RequestParam("userPasswd") String userPasswd,
                         @RequestParam("code") String code)
    {
        UserEntity user=userRepository.findByUserId(userId);
        if(user == null)
            return "用户名或者密码错误";
        if(code =="1111")
        {
            if(userPasswd==user.getPassword())
            {
                return "success";
            }
            else
            {
                return "用户名或密码错误";
            }
        }
        else
        {
            return "验证码错误";
        }
    }
}
