package es.controller;

import com.google.gson.Gson;
import es.entity.esEntity.DocEntity;
import es.repository.esRepository.DocRepository;
import es.service.SearchService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by TYF on 2018/4/8.
 */
@Controller
public class SearchController {

    @Autowired
    SearchService searchService;
    @Autowired
    DocRepository docRepository;

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
        List<DocEntity> list = searchService.searchLaw(0,10,attr,keyword);
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @RequestMapping("/multi-search-result")
    @ResponseBody
    public String multiSearchResult(@RequestParam("json")String jsonStr) throws JSONException {
        JSONArray json=new JSONArray(jsonStr);
        List<DocEntity> list = searchService.multiSearch(0,10,json);
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @RequestMapping("/similar-search-result")
    @ResponseBody
    public String similarSearchResult(@RequestParam("describe") String describe){
        List<DocEntity> list = searchService.similarSearch(0,10,describe);
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @RequestMapping("/favor")
    public String favorite(@RequestParam("docId")String docId){
        int userId = 123;//todo
        searchService.favorDoc(userId,docId);
        return "success";
    }

    @RequestMapping("recommend")
    public String recommend(@RequestParam("docId")String docId,Model model){
        model.addAttribute("docId",docId);
        return "recommendation";
    }

    @RequestMapping("/recommendation")
    @ResponseBody
    public String recommendation(@RequestParam("docId")String docId){
        DocEntity docEntity = docRepository.findOne(docId);
        List<DocEntity> list = searchService.similarSearch(0,10,docEntity.getContent());
        return new Gson().toJson(list);
    }
}
