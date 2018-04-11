package es.controller;

import com.google.gson.Gson;
import es.entity.esEntity.DocEntity;
import es.service.SearchService;
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

    @RequestMapping("/toSimpleSearch")
    @ResponseBody
    public String toSimplySearch(@RequestParam("type")String type,
                               @RequestParam("keyword") String keyword,
                               Model model){
        String attr, content;
        if (type == "标题") attr = "caseName";
        else attr = "content";
        return "simpleSearchResult?attr=" + attr + "&keyword=" + URLEncoder.encode(keyword);
    }

    @RequestMapping("/simpleSearchResult")
    public String simpleSearchResult(@RequestParam("attr")String attr,
                                     @RequestParam("keyword") String keyword,
                                     Model model){
        model.addAttribute("attr",attr);
        model.addAttribute("keyword",keyword);
        return "search-result";
    }


    @RequestMapping("/simpleSearch")
    @ResponseBody
    public String simpleSearch(@RequestParam("attr")String attr,
                               @RequestParam("keyword") String keyword){
        List<DocEntity> list = searchService.searchLaw(0,10,attr,keyword);
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @RequestMapping("/advancedSearchResult")
    public String advancedSearchResult(@RequestParam("attr")String attr,
                                     @RequestParam("keyword") String keyword,
                                     Model model){
        model.addAttribute("attr",attr);
        model.addAttribute("keyword",keyword);
        return "advanced-search";
    }
}
