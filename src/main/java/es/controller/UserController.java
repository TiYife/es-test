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

    @RequestMapping("/getVerifyCode")
    @ResponseBody
    public  String getVerifyCode(HttpSession session)
    {
        String code= VerifyCodeUtil.generateCode();
        session.setAttribute("verifyCode", code);
        return code;
    }


}
