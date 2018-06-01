package es.controller;

import com.google.gson.Gson;
import es.repository.esRepository.DocRepository;
import es.repository.jpaRepository.*;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.print.Pageable;

/**
 * Created by TYF on 2018/1/29.
 */
@Controller
public class AdminController {


    @Autowired
    public UserRepository userRepository;
    @Autowired
    public DocRepository docRepository;
    @Autowired
    private DicRepository dicRepository;
    @Autowired
    private DicLogRepository dicLogRepository;
    @Autowired
    private CaseErrorRepository  caseErrorRepository;
    @Autowired
    private NewWordRepository newWordRepository;



    @RequestMapping("/admin1")
    public String admin1(){
        return "admin1";
    }



    @RequestMapping("/admin-userlist")
    public String userList(){
        return "admin-userlist";
    }

    @RequestMapping("/list-users")
    @ResponseBody
    public String listUsers(){
        return new Gson().toJson(userRepository.findByRole(0));
    }



    @RequestMapping("/admin-adminlist")
    public String adminList(){
        return "admin-adminlist";
    }

    @RequestMapping("/list-admins")
    @ResponseBody
    public String listAdmins(){
        return new Gson().toJson(userRepository.findByRole(1));
    }



    @RequestMapping("/admin-case-import")
    public String caseImport(){
        return "admin-case-import";
    }



    @RequestMapping("/admin-caselist")
    public String caseList(){
        return "admin-caselist";
    }

    @RequestMapping("/list-cases")
    @ResponseBody
    public String listCases(){
        return new Gson().toJson(Lists.newArrayList(docRepository.findAll()));
    }



    @RequestMapping("/admin-dic")
    public String dic(){
        return "admin-dic";
    }

    @RequestMapping("/list-dics")
    @ResponseBody
    public String listDics(){
        return new Gson().toJson(dicRepository.findAll());
    }



    @RequestMapping("/admin-log")
    public String log(){
        return "admin-log";
    }

    @RequestMapping("/list-logs")
    @ResponseBody
    public String listLogs(){
        return new Gson().toJson(dicLogRepository.findAll());
    }



    @RequestMapping("/admin-newword")
    public String newword(){
        return "admin-newword";
    }

    @RequestMapping("/list-new-words")
    @ResponseBody
    public String listNewWords(){
        return new Gson().toJson(newWordRepository.findAll());
    }



    @RequestMapping("/admin-process-error-log")
    public String processErrorLog(){
        return "admin-process-error-log";
    }

    @RequestMapping("/list-errors")
    @ResponseBody
    public String listErrors(){
        return new Gson().toJson(caseErrorRepository.findAll());
    }

}
