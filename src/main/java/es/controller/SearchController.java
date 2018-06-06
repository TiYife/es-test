package es.controller;

import com.google.gson.Gson;
import es.entity.esEntity.DocEntity;
import es.entity.jpaEntity.UserEntity;
import es.repository.esRepository.DocRepository;
import es.repository.jpaRepository.UserRepository;
import es.service.SearchService;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by TYF on 2018/4/8.
 */
@Controller
public class SearchController {

    @Autowired
    SearchService searchService;
    @Autowired
    DocRepository docRepository;
    @Autowired
    UserRepository userRepository;

    private int pageSize = 203;

    @RequestMapping("/simple-search")
    public String simpleSearchResult(@RequestParam("attr")String attr,
                                     @RequestParam("keyword") String keyword,
                                     Model model){
        model.addAttribute("attr",attr);
        model.addAttribute("keyword",keyword);
        return "search-result";
    }


    @RequestMapping("/simple-search-result")
    @ResponseBody
    public String simpleSearch(@RequestParam("attr")String attr,
                               @RequestParam("keyword") String keyword){
        Page<DocEntity> page = searchService.searchLaw(0,pageSize,attr,keyword);
        return new Gson().toJson(page.getContent());
    }

    @RequestMapping("/multi-search-result")
    @ResponseBody
    public String multiSearchResult(@RequestParam("json")String jsonStr) throws JSONException {
        JSONArray json=new JSONArray(jsonStr);
        Page<DocEntity> page = searchService.multiSearch(0,pageSize,json);
        return new Gson().toJson(page.getContent());
    }

    @RequestMapping("/similar-search-result")
    @ResponseBody
    public String similarSearchResult(@RequestParam("describe") String describe){
        Page<DocEntity> page = searchService.similarSearch(0,pageSize,describe);
        return new Gson().toJson(page.getContent());
    }

    @RequestMapping("/favor")
    @ResponseBody
    public String favor(@RequestParam("docId")String docId, HttpSession session){
        UserEntity user = (UserEntity)session.getAttribute("user");
        if(user==null)
            return "你还没有登录，请登录之后再进行该操作";
        int userId= user.getId();
        return searchService.favorDoc(userId,docId);
    }

    @RequestMapping("/delete-favor")
    @ResponseBody
    public String deleteFavor(@RequestParam("docId")String docId, HttpSession session){
        UserEntity user = (UserEntity)session.getAttribute("user");
        if(user==null)
            return "你还没有登录，请登录之后再进行该操作";
        int userId= user.getId();
        searchService.deleteFavorDoc(userId,docId);
        return "success";
    }

    @RequestMapping("/favorite")
    public String favorite(Model model,HttpSession session){
        UserEntity user = (UserEntity)session.getAttribute("user");

        if(user==null) {
            model.addAttribute("msg","你还没有登录，请登录后再进行访问");
            return "error-page";
        }else{
            int userId = user.getId();
            model.addAttribute("userId",userId);
            return "favorites";
        }
    }

    @RequestMapping("list-favorite")
    @ResponseBody
    public String favoriteList(@RequestParam("userId")int userId){
        return new Gson().toJson(searchService.listFavorDocs(userId));
    }

    @RequestMapping("/recommend")
    public String recommend(@RequestParam("docId")String docId,Model model){
        model.addAttribute("docId",docId);
        return "recommendation";
    }

    @RequestMapping("/recommendation")
    @ResponseBody
    public String recommendation(@RequestParam("docId")String docId){
        DocEntity docEntity = docRepository.findOne(docId);
        Page<DocEntity> page = searchService.similarSearch(0,10,docEntity.getContent());
        return new Gson().toJson(page.getContent());
    }
}
