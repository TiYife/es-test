package es.controller;

import com.google.gson.Gson;
import es.Constant;
import es.Util.IdentityUtil;
import es.entity.jpaEntity.DicEntity;
import es.entity.jpaEntity.TxtEntity;
import es.repository.esRepository.DocRepository;
import es.repository.jpaRepository.*;
import es.service.WordSeparateService;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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
    private TxtRepository txtRepository;
    @Autowired
    private DicLogRepository dicLogRepository;
    @Autowired
    private CaseErrorRepository  caseErrorRepository;
    @Autowired
    private NewWordRepository newWordRepository;
    @Autowired
    private WordSeparateService wordSeparateService;



    @RequestMapping("/admin1")
    public String admin1(HttpSession session,Model model){
        UserEntity user = (UserEntity)session.getAttribute("user");

        if(user==null) {
            model.addAttribute("msg","你还没有登录，请登录后再进行访问");
            return "error-page";
        }
        if(user.getRole()!=1){
            model.addAttribute("msg","你没有权限访问该页面");
            return "error-page";
        }
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
                            HttpServletRequest request,
                            HttpSession session)
    {
        if(dicRepository.findFirstByWord(word)!=null)
            return "已存在该词组";
        int re1=wordSeparateService.addDic(word,sepaType);
        int re2=wordSeparateService.saveDic();
        if(re1>0&&re2>0)
        {
            DicEntity dic=new DicEntity();
            dic.setWord(word);
            Date day=new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dic.setCreateTime(df.format(day));
            dic.setSepaType(sepaType);
            dic.setType(type);
            //UserEntity user=(UserEntity)session.getAttribute("user");
            //dic.setCreateUserId(user.getId());
            String uId= IdentityUtil.getCookieValue(request,"userId");
            dic.setCreateUserId(Integer.parseInt(uId));
            dicRepository.save(dic);
            return "success";
        }
        else {
            return "词典保存失败,"+re1+re2;
        }
    }

    @RequestMapping("/upload-dic")
    @ResponseBody
    public String uploadDic(@RequestParam("file")MultipartFile file, HttpServletRequest request,HttpSession session){
            int totalNum=0;
            int successNum=0;
                try {
                    String errorTxt="";
            String content=new String(file.getBytes(), "utf-8");
            //String content=new String( contentGBK.getBytes("utf-8") , "utf-8");
                    //String content=wordSeparateService.getUTF8StringFromGBKString(contentGBK);
            String[] lines=content.split("\r\n");
            totalNum=lines.length;

            for(int i=0;i<lines.length;i++)
            {
                String line=lines[i];
                String[] attrs=line.split(" |\t");
                if(dicRepository.findFirstByWord(attrs[0])!=null)
                {
                    errorTxt+=(i+1)+"\t"+attrs[0]+"\t"+"已存在"+"\n";
                    continue;
                }
                int re1=wordSeparateService.addDic(attrs[0],attrs[1]);
                int re2=wordSeparateService.saveDic();
                if(re1>0&&re2>0)
                {
                    DicEntity dic=new DicEntity();
                    dic.setWord(attrs[0]);
                    Date day=new Date();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    dic.setCreateTime(df.format(day));
                    dic.setSepaType(attrs[1]);
                    dic.setType(attrs[2]);
                    //UserEntity user=(UserEntity)session.getAttribute("user");
                    //dic.setCreateUserId(user.getId());
                    String uId= IdentityUtil.getCookieValue(request,"userId");
                    dic.setCreateUserId(Integer.parseInt(uId));
                    //session.refresh(object)
                    dicRepository.save(dic);
                    successNum++;
                }
                else {
                    errorTxt+=(i+1)+"\t"+attrs[0]+"\t"+"无法存入词典"+re1+re2+"\n";
                    continue;
                }

            }
            String fileAddress=Constant.dicFileLocation + UUID.randomUUID().toString() + file.getOriginalFilename();
            wordSeparateService.createPreDirectory(fileAddress);
                    file.transferTo(new File(fileAddress));
                    wordSeparateService.stringToRead(errorTxt,fileAddress+".error",false);
                }catch (Exception e)
                {
                    return e.getMessage();
                }
        return "{success:'导入"+totalNum+"条词组，"+"成功"+successNum+"条词"+(successNum==totalNum?"”，错误详情请查看错误日志文件":"")+"'}";
    }



    @RequestMapping("/get-txt")
    @ResponseBody
    public String getTxt(@RequestParam("id") String id,
                         HttpSession session){
        try {
            TxtEntity txtEntity= txtRepository.findOne(id);
            if(txtEntity!=null)
                return wordSeparateService.readToString(txtEntity.getLocation());
            else
                return "error:找不到该文件";
        }catch (Exception e)
        {
            return "error:找不到该文件路径";
        }
    }

}
