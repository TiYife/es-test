package es.controller;

import es.service.impl.WordSeparateServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by TYF on 2018/5/7.
 */
@Controller
public class PageController {

    private WordSeparateServiceImpl wordSeparateService = new WordSeparateServiceImpl();

    @RequestMapping("/")
    public String toIndex(HttpServletRequest request ,HttpSession session){
        return "index1";
    }

    @RequestMapping("/index")
    public String toIndex2(HttpServletRequest request ,HttpSession session){
        return "index1";
    }

    @RequestMapping("/admin")
    public String toAdmin(){
        return "admin1";
    }

    @RequestMapping("/advanced-search")
    public String toAdvancedSearch(){
        return "advanced-search1";
    }

    @RequestMapping("/favorites")
    public String toFavorites(){
        return "favorites";
    }

    @RequestMapping("/test")
    public String test(HttpServletRequest request, String name){
        request.setAttribute("text","hhh");
        String res="";
        if(name!=null)
            res=wordSeparateService.getnn(name);
        request.setAttribute("re",res);
        return "test_page";
    }
}
