package es.controller;

import es.Util.VerifyCodeUtil;
import es.entity.jpaEntity.UserEntity;
import es.repository.jpaRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

import static es.Constant.timeFormat;

/**
 * Created by TYF on 2018/1/29.
 */
@Controller
public class UserController {


    @Autowired
    public UserRepository userRepository;

    @RequestMapping("/uLogin")
    @ResponseBody
    public  String login(@RequestParam("userId") String userId,
                         @RequestParam("userPasswd") String userPasswd,
                         @RequestParam("code") String code,
                         HttpSession session)
    {
        String verifyCode = session.getAttribute("verifyCode").toString();
        if(code.toLowerCase().equals(verifyCode.toLowerCase()))
        {
            UserEntity user=userRepository.findByUserName(userId);
            if(user == null)
                return "用户名或者密码错误";
            if(userPasswd.equals(user.getPassword()))
            {
                session.setAttribute("user", user);
                return "success:"+user.getRole()+user.getId();
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

    @RequestMapping("uLogout")
    public  String login(@RequestParam("userId") String userId,
                         HttpSession session)
    {
        UserEntity user = (UserEntity)session.getAttribute("user");
        if(user==null) return "你还没有登录";
        session.removeAttribute("user");
        session.invalidate();
        return "success";
    }

    @RequestMapping("/uRegister")
    @ResponseBody
    public  String register(@RequestParam("userId") String userId,
                            @RequestParam("userPasswd") String userPasswd,
                            @RequestParam("userPasswdConfirm") String userPasswdConfirm,
                            @RequestParam("userEmail") String userEmail,
                         @RequestParam("code") String code,
                         HttpSession session)
    {
        String verifyCode = session.getAttribute("verifyCode").toString();
        if(code.toLowerCase().equals(verifyCode.toLowerCase()))
        {
            if(userPasswd.equals(userPasswdConfirm))
            {
                    UserEntity newUser=new UserEntity();
                    newUser.setUserName(userId);
                    newUser.setPassword(userPasswd);
                    newUser.setEmail(userEmail);
                    newUser.setRole(2);
                    Date day=new Date();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    newUser.setRegisterTime(df.format(day));
                    userRepository.save(newUser);
                    return "success";

            }
            else
            {
                return "确认密码不一致";
            }
        }
        else
        {
            return "验证码错误";
        }
    }

    @RequestMapping("/getVerifyCode")
    @ResponseBody
    public  String getVerifyCode(HttpSession session)
    {
        String code= VerifyCodeUtil.generateCode();
        session.setAttribute("verifyCode", code);
        return code;
    }

    @RequestMapping("/editPasswd")
    @ResponseBody
    public  String editPasswd(int userId, String passwd)
    {
       UserEntity user = userRepository.findById(userId);
       user.setPassword(passwd);
       userRepository.save(user);
       return "success";
    }

    @RequestMapping("/delete-user")
    @ResponseBody
    public  String deleteUser(int userId)
    {
        UserEntity user = userRepository.findById(userId);
        userRepository.delete(user);
        return "success";
    }

    @RequestMapping("/add-admin")
    @ResponseBody
    public String addAdmin(String name,String passwd,String email)
    {
        UserEntity user = new UserEntity();
        user.setUserName(name);
        user.setPassword(passwd);
        user.setEmail(email);
        user.setRegisterTime(timeFormat.format(new Date()));
        user.setRole(1);
        userRepository.save(user);
        return "success";
    }

}
