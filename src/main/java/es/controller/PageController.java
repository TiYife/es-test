package es.controller;

import es.service.impl.WordSeparateServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by TYF on 2018/5/7.
 */
@Controller
public class PageController {

    private WordSeparateServiceImpl wordSeparateService = new WordSeparateServiceImpl();

    @RequestMapping("/")
    public String toIndex(){
        return "index";
    }

    @RequestMapping("/index")
    public String toIndex2(){
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
