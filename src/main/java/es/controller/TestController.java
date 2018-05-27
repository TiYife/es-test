package es.controller;

import com.sun.jna.Native;
import es.entity.esEntity.DocEntity;
import es.repository.jpaRepository.XmlRepository;
import es.entity.word.Primitive;
import es.service.NLPTRService;
import es.service.SaveService;
import es.service.SearchService;
import es.entity.word.WordSimilarity;
import es.service.impl.WordSeparateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static es.Constant.HFWord_PATH;

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
    private XmlRepository xmlRepository;
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

    @RequestMapping("ssw")
    @ResponseBody
    public String rch( Model model){
        //return wordSeparateService.getHFWordFormFiles("all");
        List<DocEntity> docEntities=searchService.searchLaw(1,20,"content","火车");

        List<DocEntity> list=searchService.allLaw();
        try {
        int size=list.size();
        int a = 20,b=100,d=30;
        double  c = 0.5;
        NLPTRService instance = (NLPTRService) Native.loadLibrary(System.getProperty("user.dir") + "\\source\\NLPIR", NLPTRService.class);
            int init_flag = instance.NLPIR_Init("", 1, "0");
            String resultString = null;
            if (0 == init_flag) {
                resultString = instance.NLPIR_GetLastErrorMsg();
                System.err.println("初始化失败！\n" + resultString);
                return "";
            }
        Map<String, Integer> Word = new HashMap<String, Integer>();

        int k=0;
        for(DocEntity doc :list)
        {
            k++;
            instance.NLPIR_ParagraphProcess(doc.getContent(), 1);
            String docAfterProcess = instance.NLPIR_ParagraphProcess(doc.getContent(),0);
            String[] wlist=docAfterProcess.split(" ");
            for(String s:wlist)
            {
                if(s.equals("")) continue;
                if(Word.containsKey(s))
                    Word.put(s,Word.get(s)+1);
                else
                    Word.put(s,1);
                for(int i=0;i<s.length();i++)
                    if(wlist[i].equals(s))
                        wlist[i]="";
            }
            for (String w : Word.keySet()) {
                //map.keySet()返回的是所有key的值
                int f=Word.get(w);
                if(k>a&&k<b&&(double)f/(double)size<c) Word.remove(w);
                if(k>b&&f<d) Word.remove(w);
            }
        }
        String result="";
        String sp="";
        for (String w : Word.keySet()) {
            result+=sp+w;
            sp="\n";
        }
        wordSeparateService.stringToRead(result,HFWord_PATH,false);
        return "success";
    }catch (Exception e)
    {
        return e.getMessage();
    }
        //return "";
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

}
