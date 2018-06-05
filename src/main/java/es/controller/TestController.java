package es.controller;

import com.google.gson.Gson;
import es.entity.esEntity.DocEntity;
import es.entity.jpaEntity.DicEntity;
import es.entity.jpaEntity.NewWordEntity;
import es.entity.word.Primitive;
import es.entity.word.WordSimilarity;
import es.repository.esRepository.DocRepository;
import es.repository.jpaRepository.DicRepository;
import es.service.SearchService;
import es.service.impl.WordSeparateServiceImpl;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static es.Constant.timeFormat;

/**
 * Created by TYF on 2018/1/29.
 */
@Controller
public class TestController {

    @Autowired
    private SearchService searchService;
    @Autowired
    private DicRepository dicRepository;
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
                         @RequestParam("content") String content, Model model) {
        List<DocEntity> docEntities = searchService.searchLaw(1, 20, attr, content);
        model.addAttribute("list", docEntities);
        return "searchResult";
    }

    @RequestMapping("ss")
    @ResponseBody
    public String search(@RequestParam("attr") String attr, Model model) {
        String primitive = "攻打";
        String r = "";
        List<Integer> list = Primitive.getParents(attr);
        for (Integer i : list) {
            r += i.toString() + " | ";
        }
        test_loadGlossary();
        test_disPrimitive();
        test_simPrimitive();
        test_simWord();
        return r;
    }

    /*@RequestMapping("ssw")
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
    }*/


    public void test_loadGlossary() {
        WordSimilarity.loadGlossary();
    }

    /**
     * test the method {@link WordSimilarity#disPrimitive(String, String)}.
     */
    public void test_disPrimitive() {
        int dis = WordSimilarity.disPrimitive("雇用", "争斗");
        System.out.println("雇用 and 争斗 dis : " + dis);
    }

    public void test_simPrimitive() {
        double simP = WordSimilarity.simPrimitive("雇用", "争斗");
        System.out.println("雇用 and 争斗 sim : " + simP);
    }

    public void test_simWord() {
        String word1 = "牛";
        String word2 = "猪";
        double sim = WordSimilarity.simWord(word2, word1);
        System.out.println(sim);
    }

    @RequestMapping("sss")
    @ResponseBody
    public String sss() {
        List<DocEntity> docEntities = Lists.newArrayList(docRepository.findAll());
        return wordSeparateService.getHFWordFormFiles("all", docEntities);
    }

    @RequestMapping("/getWord")
    @ResponseBody
    public String test1() {
        List<DicEntity> list = new ArrayList<>();
        DicEntity entity;
        entity = new DicEntity();
        entity.setId(1);
        entity.setWord("审判人");
        entity.setType("spr");
        entity.setSepaType("标志词词典");
        entity.setCreateUserId(123);
        entity.setCreateTime("2018-06-02 03:02:05");
        list.add(entity);
        return new Gson().toJson(list);
    }


    @RequestMapping("/getManyWord")
    @ResponseBody
    public String test3() {
        List<DicEntity> list = new ArrayList<>();
        DicEntity entity;
        entity = new DicEntity();
        entity.setId(1);
        entity.setWord("审判人");
        entity.setType("spr");
        entity.setSepaType("标志词词典");
        entity.setCreateUserId(123);
        entity.setCreateTime("2018-06-02 03:02:05");
        list.add(entity);

        entity = new DicEntity();
        entity.setId(2);
        entity.setWord("上诉人");
        entity.setType("dsr");
        entity.setSepaType("标志词词典");
        entity.setCreateUserId(123);
        entity.setCreateTime("2018-06-02 03:02:45");
        list.add(entity);

        entity = new DicEntity();
        entity.setId(3);
        entity.setWord("西岔镇");
        entity.setType("na");
        entity.setSepaType("地区词典");
        entity.setCreateUserId(123);
        entity.setCreateTime("2018-06-02 03:02:45");
        list.add(entity);

        entity = new DicEntity();
        entity.setId(4);
        entity.setWord("被告人");
        entity.setType("dsr");
        entity.setSepaType("标志词词典");
        entity.setCreateUserId(123);
        entity.setCreateTime("2018-06-02 03:02:45");
        list.add(entity);
        return new Gson().toJson(list);
    }

    @RequestMapping("/getbf")
    @ResponseBody
    public String test2() {
        List<NewWordEntity> list = new ArrayList<>();
        NewWordEntity entity = new NewWordEntity();
        entity.setId(1);
        entity.setWord("居间合同");
        entity.setCreateTime(timeFormat.format(new Date()));
        entity.setCreateLocation("a681599a-2d8e-4895-b257-b8e3074c64c8裁定书.txt");
        entity.setContext("姜善伟与徐钊、涡阳县长建建筑材料有限公司居间合同纠纷再审民事裁定书");
        entity.setSepaType("-");
        entity.setRemark("-");
        list.add(entity);
        return new Gson().toJson(list);
    }
    @RequestMapping("/getaf")
    @ResponseBody
    public String test4() {
        List<NewWordEntity> list = new ArrayList<>();
        NewWordEntity entity = new NewWordEntity();
        entity.setId(1);
        entity.setWord("居间合同");
        entity.setCreateTime(timeFormat.format(new Date()));
        entity.setCreateLocation("a681599a-2d8e-4895-b257-b8e3074c64c8裁定书.txt");
        entity.setContext("姜善伟与徐钊、涡阳县长建建筑材料有限公司居间合同纠纷再审民事裁定书");
        entity.setSepaType("n");
        entity.setRemark("已处理");
        list.add(entity);
        return new Gson().toJson(list);
    }
}
