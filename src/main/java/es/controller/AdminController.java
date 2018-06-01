package es.controller;

import es.repository.jpaRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by TYF on 2018/1/29.
 */
@Controller
public class AdminController {


    @Autowired
    public UserRepository userRepository;



    @RequestMapping("/admin1")
    public String admin1(){
        return "admin1";
    }
    @RequestMapping("/admin-adminlist")
    public String adminlist(){
        return "admin-adminlist";
    }
    @RequestMapping("/admin-case-import")
    public String casemport(){
        return "admin-case-import";
    }
    @RequestMapping("/admin-caselist")
    public String caselist(){
        return "admin-caselist";
    }
    @RequestMapping("/admin-dic")
    public String dic(){
        return "admin-dic";
    }
    @RequestMapping("/admin-log")
    public String log(){
        return "admin-log";
    }
    @RequestMapping("/admin-newword")
    public String newword(){
        return "admin-newword";
    }
    @RequestMapping("/admin-process-error-log")
    public String processErrorLog(){
        return "admin-process-error-log";
    }
    @RequestMapping("/admin-userlist")
    public String userlist(){
        return "admin-userlist";
    }


}
