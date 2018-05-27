package es.controller;

import es.entity.esEntity.DocEntity;
import es.repository.esRepository.DocRepository;
import es.repository.jpaRepository.XmlRepository;
import es.entity.word.Primitive;
import es.service.SaveService;
import es.service.SearchService;
import es.entity.word.WordSimilarity;
import es.service.impl.WordSeparateServiceImpl;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by TYF on 2018/1/29.
 */
@Controller
public class TestController {

    @Autowired
    private SearchService searchService;
    @Autowired
    private SaveService saveService;
    @Autowired
    private DocRepository docRepository;
    private WordSeparateServiceImpl wordSeparateService = new WordSeparateServiceImpl();

//    @RequestMapping("/add")
//    @ResponseBody
//    public String test(){
//        DocEntity law = new DocEntity();
//        law.setContent("just for testing");
//        return searchService.saveLaw(law);
//    }
//
//    @RequestMapping("/query")
//    @ResponseBody
//    public String query(@RequestParam String content){
//        return searchService.searchLaw(null,null,content).toString();
//    }
//
////    @RequestMapping("/record")
////    @ResponseBody
////    public String record(){
////        if(saveService.recordDocs(new File(Constant.FILE_LOCATION)))return "success";
////        return "fail";
////    }
////
////    @RequestMapping("/read")
////    @ResponseBody
////    public String read(){
////        File file = new File(Constant.FILE_LOCATION+"数据库系统概论知识点整理.txt");
////        return saveService.readDoc(file);
////    }
//
//    @RequestMapping("/save")
//    @ResponseBody
//    public String save(){
//        File file = new File(Constant.FILE_LOCATION+"20140107\\安徽\\安徽省安庆市中级人民法院\\行政案件\\bbf10813-8274-48e7-b6af-56dc1b4a29c5.xml");
//        if (saveService.saveDoc(file))return "success";
//        return "false";
//    }
//
//    @RequestMapping("/saves")
//    @ResponseBody
//    public String saves(){
//        File file = new File(Constant.FILE_LOCATION);
//        if (saveService.saveDocs(file))return "success";
//        return "false";
//    }

    @RequestMapping("search")
    public String search(@RequestParam("attr") String attr,
                         @RequestParam("content") String content, Model model){
        List<DocEntity> docEntities=searchService.searchLaw(1,20,attr,content);
        model.addAttribute("list",docEntities);
        return "searchResult";
    }
    @RequestMapping("ss")
    @ResponseBody
    public String search(@RequestParam("attr") String attr, Model model){
        String primitive = "攻打";
        String r="";
        List<Integer> list = Primitive.getParents(attr);
        for(Integer i : list){
            r+= i.toString()+" | ";
        }
        test_loadGlossary();
        test_disPrimitive();
        test_simPrimitive();
        test_simWord();
        return r;
    }

    public void test_loadGlossary(){
        WordSimilarity.loadGlossary();
    }
    /**
     * test the method {@link WordSimilarity#disPrimitive(String, String)}.
     */
    public void test_disPrimitive(){
        int dis = WordSimilarity.disPrimitive("雇用", "争斗");
        System.out.println("雇用 and 争斗 dis : "+ dis);
    }

    public void test_simPrimitive(){
        double simP = WordSimilarity.simPrimitive("雇用", "争斗");
        System.out.println("雇用 and 争斗 sim : "+ simP);
    }
    public void test_simWord(){
        String word1 = "牛";
        String word2 = "猪";
        double sim = WordSimilarity.simWord(word2, word1);
        System.out.println(sim);
    }

    @RequestMapping("sss")
    @ResponseBody
    public String sss(){
        List<DocEntity> docEntities = Lists.newArrayList(docRepository.findAll());
        return wordSeparateService.getHFWordFormFiles("all",docEntities);
    }
}
