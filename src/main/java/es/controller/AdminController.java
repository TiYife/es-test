package es.controller;

import com.google.gson.Gson;
import es.entity.jpaEntity.DicEntity;
import es.entity.jpaEntity.UserEntity;
import es.repository.esRepository.DocRepository;
import es.repository.jpaRepository.*;
import es.service.WordSeparateService;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    @Autowired
    private WordSeparateService wordSeparateService;



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

    @RequestMapping("add-dic")
    @ResponseBody
    public  String register(@RequestParam("word") String word,
                            @RequestParam("type") String type,
                            @RequestParam("sepaType") String sepaType,
                            HttpSession session)
    {
        if(dicRepository.findFirstByWord(word)!=null)
            return "已存在该词组";
        int re=wordSeparateService.addDic(word,type);
        if(re>0)
        {
            DicEntity dic=new DicEntity();
            dic.setWord(word);
            Date day=new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dic.setCreateTime(df.format(day));
            dic.setSepaType(sepaType);
            dic.setType(type);
            UserEntity user=(UserEntity)session.getAttribute("user");
            dic.setCreateUserId(user.getId());
            dicRepository.save(dic);
            return "success";
        }
        else {
            return "词典保存失败";
        }
    }

    @RequestMapping("/upload-dic")
    @ResponseBody
    public String uploadDic(@RequestParam("file")MultipartFile file, HttpServletRequest request){
//        String uId=IdentityUtil.getCookieValue(request,"userId");
//        if(uId==null || uId.equals("null"))    return "not login";

        return "";
    }

}
