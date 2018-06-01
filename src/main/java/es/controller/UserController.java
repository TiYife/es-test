package es.controller;

import com.google.gson.Gson;
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

/**
 * Created by TYF on 2018/1/29.
 */
@Controller
public class UserController {


    @Autowired
    public UserRepository userRepository;

    @RequestMapping("/uLogin")
    @ResponseBody
    public  String login(@RequestParam("userId") int userId,
                         @RequestParam("userPasswd") String userPasswd,
                         @RequestParam("code") String code,
                         HttpSession session)
    {
        String verifyCode = session.getAttribute("verifyCode").toString();
        if(code.toLowerCase().equals(verifyCode.toLowerCase()))
        {
            UserEntity user=userRepository.findById(userId);
            if(user == null)
                return "用户名或者密码错误";
            if(userPasswd.equals(user.getPassword()))
            {
                session.setAttribute("user", user);
                return "success:"+user.getRole()+user.getUserName();
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

    @RequestMapping("/uRegister")
    @ResponseBody
    public  String register(@RequestParam("userId") int userId,
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
                UserEntity user=userRepository.findById(userId);
                if(user == null){
                    UserEntity newUser=new UserEntity();
                    newUser.setId(userId);
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
                    return "用户名已存在";
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

}
