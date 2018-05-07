package es.controller;

import es.entity.jpaEntity.UserEntity;
import es.repository.jpaRepository.UserRepository;
import es.service.impl.WordSeparateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
        UserEntity user=userRepository.findById(userId);
        if(user == null)
            return "用户名或者密码错误";
        if(code =="1111")
        {
            if(userPasswd==user.getPassword())
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


}
