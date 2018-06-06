package es;


import com.sun.jna.Native;
import es.Util.FileUtil;
import es.entity.esEntity.DocEntity;
import es.entity.word.WordSimilarity;
import es.repository.esRepository.DocRepository;
import es.service.NLPTRService;
import es.service.SaveService;
import es.service.SearchService;
import es.service.impl.WordSeparateServiceImpl;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static es.Constant.xmlLocation;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

/**
 * Created by 13051 on 2018/2/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TEST {

    @Autowired
    SaveService saveService;
    @Autowired
    SearchService searchService;
    @Autowired
    DocRepository docRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    private WordSeparateServiceImpl wordSeparateService = new WordSeparateServiceImpl();

    @Test
    public void testXml() {

    }

//    @Test
//    public void testSave(){
//        File file = new File(Constant.FILE_LOCATION+"20140107\\安徽\\安徽省安庆市中级人民法院\\行政案件\\bbf10813-8274-48e7-b6af-56dc1b4a29c5.xml");
//        saveService.saveDoc(file);
//    }

//    @Test
//    public void testSearch(){
//        List<DocEntity> docEntities=searchService.searchLaw(1,10,"content","被告人");
//    }
//
//    @Test
//    public void testSimilarSearch(){
//        List<DocEntity> docEntities = searchService.similarSearch(0,10,"宋皓以“其与黄某某是合作关系，借款与担保是两人之间约定的特殊合作方式，其参与了公司的日常经营管理活动；原判认定事实不清，适用法律错误”为由，向贵州省高级人民法院申诉，该院于2011年12月16日作出（2011）黔高调刑监字第25号通知驳回申诉。");
//        assert docEntities.size()>0;
//    }

    @Test
    public void testSimilar2Search(){
        QueryBuilder queryBuilder = QueryBuilders.moreLikeThisQuery("content").like("宋皓以“其与黄某某是合作关系，借款与担保是两人之间约定的特殊合作方式，其参与了公司的日常经营管理活动；原判认定事实不清，适用法律错误”为由，向贵州省高级人民法院申诉，该院于2011年12月16日作出（2011）黔高调刑监字第25号通知驳回申诉。");
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
                .add(boolQuery().should(queryBuilder),
                        ScoreFunctionBuilders.weightFactorFunction(1000))
                .scoreMode("sum").setMinScore(10.0F);
        Pageable pageable = new PageRequest(0, 10);
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
        List<DocEntity> list = docRepository.search(searchQuery).getContent();
    }

    @Test
    public void testKeyWord(){
        wordSeparateService.NLPTR_Init();
        String s="陕西省延川县人民法院&amp;#xA;民 事 判 决 书&amp;#xA;（2013）延民初字第00359号&amp;#xA;原告杨世凯，男，1971年4月10日出生，汉族，文盲，延川县贺家湾乡杨家山行政村人，农民，住延川县延川镇南关街。&amp;#xA;被告冯文生，男，成年，汉族，高中文化，延川县禹居镇人，居民，住延川县人民医院家属楼。&amp;#xA;原告杨世凯与被告冯文生合伙协议纠纷一案，本院于2013年7月29日受理，在法定期限内向被告送达了起诉状副本、应诉通知书、举证通知书、告知合议庭组成人员通知书及开庭传票等法律文书。并依法组成合议庭于2013年9月24日公开开庭审理了本案，原告杨世凯到庭参加了诉讼，被告冯文生经传票传唤未到庭参加诉讼。本案现已审理终结。&amp;#xA;原告诉称：2012年原告与被告合伙经营树苗生意。2013年4月21日经双方协商决定树苗折价80000元由被告一人经营。协议签订后，被告称暂时无钱给付，给原告出具了80000元欠据一支，约定第二天给付。后经原告多次催要，被告一直拒付。现起诉要求被告偿还欠款80000元及利息。&amp;#xA;原告向法庭提交双方签订的协议（收条）、被告出具的欠据来证明被告冯文生欠原告80000元未偿还的事实。&amp;#xA;被告冯文生未答辩，未向法庭提交证据，也未到庭对原告的证据进行质证。&amp;#xA;经庭审举证、质证、认证后认为：原告向法庭提交的协议和欠据因被告冯文生未向法庭提交反驳原告主张的证据，也未到庭对原告的证据进行质证，故该证据本院予以采信，可以作为定案的依据。&amp;#xA;根据对以上证据的认定，本案可以确认的事实为：&amp;#xA;2012年开始原告杨世凯与被告冯文生合伙经营树苗生意。2013年4月21日经双方协商决定将树苗折价80000元由被告冯文生一人经营，并约定被告冯文生当日给付原告杨世凯树苗折价款80000元，被告因当日无钱给付，给原告出具了80000元欠据一支，约定第二天给付，但经原告杨世凯多次催要被告一直未付。&amp;#xA;本院认为，原告杨世凯与被告冯文生合伙终止后，达成的协议是双方当事人真实意思的表示，协议内容未违反法律法规的规定，依法应当保护。被告冯文生应当按照约定的时间清偿欠款，故原告要求被告清偿80000元树苗折价款的诉讼请求本院予以支持。原告要求被告支付利息的请求因无法律依据本院不予支持。依据《中华人民共和国民法通则》第八十四条之规定，判决如下：&amp;#xA;由被告冯文生在判决生效后5日内清偿原告杨世凯树苗折价款80000元。&amp;#xA;如果未按本判决指定的期间给付金钱义务，应当按照《中华人民共和国民事诉讼法》第二百五十三条之规定，加倍支付迟延履行期间的债务利息。&amp;#xA;案件受理费1800元由被告冯文生负担。&amp;#xA;如不服本判决，可在判决书送达之日起十五日内向本院递交上诉状，并按对方当事人的人数提出副本，上诉于陕西省延安市中级人民法院。&amp;#xA;审　判　长　　王玉峰&amp;#xA;审　判　员　　张　勇&amp;#xA;人民陪审员　　白开雄&amp;#xA;二〇一三年十月二十九日&amp;#xA;书　记　员　　杜延良&amp;#xA;本案所依据的法律条文：&amp;#xA;《中华人民共和国民法通则》第八十四条债是按照合同的约定或者依照法律的规定，在当事人之间产生的特定的权利和义务关系，享有权利的人是债权人，负有义务的人是债务人。&amp;#xA;债权人有权要求债务人按照合同的约定或者依照法律的规定履行义务。";
        String ss=wordSeparateService.getKeyWord(s);
        wordSeparateService.NLPTR_Exit();
        String sss;

    }
    @Test
    public void getKeyWords()
    {
        //初始化
        NLPTRService instance = (NLPTRService) Native.loadLibrary(System.getProperty("user.dir") + "\\source\\NLPIR", NLPTRService.class);
        int init_flag = instance.NLPIR_Init("", 1, "0");
        String resultString = null;
        if (0 == init_flag) {
            resultString = instance.NLPIR_GetLastErrorMsg();
            System.err.println("初始化失败！\n" + resultString);
        }

        String sInput = "上诉人徐惠、黄农兵、黄琪、黄小岚、武贤勇因与被上诉人六安元和置业集团有限公司房屋买卖合同纠纷一案，不服安徽省六安市中级人民法院于2014年4月21日作出的（2013）六民一初字第00002号驳回起诉的民事裁定，向本院提起上诉。本院受理后，依法组成合议庭审理了本案。\n" +
                "原审裁定认为，徐惠等五人与六安元和置业集团有限公司在履行案涉《元和山庄商品房预售合同》、《商品房买卖合同》及《补充协议》过程中，并未产生争议，徐惠等五人的本次起诉，不符合民事诉讼法规定的起诉条件。据此，案经原审法院审判委员会讨论决定，依照《中华人民共和国民事诉讼法》第一百一十九条之规定，裁定：驳回徐惠、黄农兵、黄琪、黄小岚、武贤勇的起诉。\n" +
                "徐惠等五人上诉称：因六安元和置业集团有限公司不履行合同及协议约定的义务，导致双方产生纠纷。原审驳回徐惠等五人起诉的法律依据不明确。请求二审法院撤销原审裁定。\n" +
                "六安元和置业集团有限公司清算组未向本院提交书面答辩状。";
        try {
            //double i1= instance.NLPIR_FileProcess("E:\\桌面存放\\测试\\12.txt","E:\\桌面存放\\测试\\12.txt"+".stxt",1);
            //String ss=wordSeparateService.readToString("E:\\桌面存放\\测试\\12.txt");
            //String ii=instance.NLPIR_ParagraphProcess(ss,1);
            //String sss=wordSeparateService.stringToRead(ii,"E:\\桌面存放\\测试\\1212.txt");
            //int i2=instance.NLPIR_DelUsrWord("法定代表人 dlr");
            //int i2=instance.NLPIR_DelUsrWord("申请人\tdsr");
            //int i1=instance.NLPIR_ImportUserDict("E:\\毕业设计\\es-test\\dic\\标志词词典.txt");
            //int i3=instance.NLPIR_SaveTheUsrDic();
            //String s= instance.NLPIR_GetNewWords("克隆",50,false);
            //String s=instance.NLPIR_ParagraphProcess("姓名权纠纷是一个案由",1);
            //String iii=ii.replaceAll("\r\n","\n");
            //String[] lines = ii.split("\r");

            wordSeparateService.fileProcessAndSave("E:\\桌面存放\\测试\\12.txt","E:\\桌面存放\\测试","E:\\桌面存放\\测试\\1223.txt");
            //String[] fileLines=fileString.split("\r\n");


            instance.NLPIR_Exit();

        } catch (Exception e) {
            System.out.println("错误信息：");
            e.printStackTrace();
        }
    }

    @Test
    public void testJava11()
    {
        wordSeparateService.NLPTR_Init();
        //wordSeparateService.stringToRead("","E:\\桌面存放\\测试\\errorTest.txt",false);
        wordSeparateService.multiFileProcessAndSave("E:\\桌面存放\\测试3\\20150104","E:\\桌面存放\\测试3\\20150104","E:\\桌面存放\\测试4");
        //wordSeparateService.NLPTR_Exit();
        //String s=System.getProperty("user.dir");
        /*WordSimilarity.loadGlossary();
        int dis = WordSimilarity.disPrimitive("雇用", "争斗");
        double simP = WordSimilarity.simPrimitive("雇用", "争斗");
        double sim = WordSimilarity.simWord("牛", "猪");
        //*/
        //String fileAddress="E:\\桌面存放\\测试\\12.txt";
        /*String fileString=wordSeparateService.readToString(fileAddress);
        String fileStringPre = wordSeparateService.filePreProcess(fileString);
        wordSeparateService.stringToRead(fileStringPre,"E:\\桌面存放\\测试\\13pre1.txt");*/
        //wordSeparateService.fileProcessAndSave(fileAddress,"E:\\桌面存放\\测试","E:\\桌面存放\\测试2");
    }
    //结构化处理模块测试
    @Test
    public void testJava1()
    {
        wordSeparateService.NLPTR_Init();
        wordSeparateService.multiFileProcessAndSave("E:\\系统测试\\案例元数据\\20150104","E:\\系统测试\\案例元数据\\20150104","E:\\系统测试\\处理结果\\20150104");
    }

    //关键词提取模块测试
    @Test
    public void testJava2()
    {
        wordSeparateService.NLPTR_Init();
        String fileString =wordSeparateService.readToString( "E:\\系统测试\\案例元数据\\20150104\\安徽\\1275\\民事案件\\（2014）枞民一初字第01731号_e4b30124-6409-43f1-9c59-eb70f0cd327a判决书.txt");
        //wo
    }

    //语义相似度模块测试
    @Test
    public void testJava3()
    {
        double sim1 = WordSimilarity.simWord("喜欢", "喜爱");
        double sim2 = WordSimilarity.simWord("激动", "喜欢");
        System.out.println(sim1);
        System.out.println(sim2);
    }

    //高频词组提取测试
    @Test
    public void testJava4()
    {
        String fileString =wordSeparateService.readToString( "E:\\系统测试\\案例元数据\\20150104\\安徽\\1275\\民事案件\\（2014）枞民一初字第01731号_e4b30124-6409-43f1-9c59-eb70f0cd327a判决书.txt");
        fileString=fileString.replaceAll("(\r\n)+\t*","\r\n");
        fileString=fileString.replaceAll(" ","");
        fileString=fileString.replaceAll("([^　])　(?!　)","$1");
        fileString=fileString.replaceAll("((<[a-z0-9]+?=|<[a-z0-9]+?>|\\{C\\}<!--).+|.+(</[a-z0-9]+?>|-->))\r\n","");
        fileString=fileString.replaceAll("(</?[a-z0-9]+?>)+\r\n","");
        fileString=fileString;
    }

    @Test
    public void testJavaaa()
    {
        String s=wordSeparateService.readToString("E:\\测试\\案例名称无.txt");
        String[] nameList=s.split("\r\n");
        for(int i=0;i<nameList.length;i++)
        {
            File file = new File(nameList[i]);
            File file2 = new  File("E:\\测试\\案件名称无\\"+nameList[i].substring(11,nameList[i].length()));
            String news="E:\\测试\\案件名称无\\"+nameList[i].substring(11,nameList[i].length());
            wordSeparateService.createPreDirectory(news);
            //file.renameTo(file2);
            try {
                Files.copy(file.toPath(), file2.toPath());
            }catch (Exception e){

            }
            //file.renameTo(file2);
        }
    }

    @Test
    public void testS2r()
    {
        //wordSeparateService.getHFWordFormFiles("all");
    }



    public String getType(String s)
    {
        switch (s)
        {
            case "n":return "名词";
            case "ns":return "地名";
            case "v":return "动词";
            default:return "名词";
        }
    }

    public boolean isStay(int i)
    {
        switch (i)
        {
            case 0:
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            case 12:
            case 13:
            case 18:
                return false;
            default:return true;
        }
    }

    public String isStays(int i)
    {
        switch (i)
        {
            case 1: return " ";
            case 2:return "";
            case 3:return " ";
            case 17:return "  ";
            default:return "   ";
        }
    }

    @Test
    public void testFileMove(){
        FileUtil.dirMappingMove(Constant.originalDocLocation+"20140107",Constant.originalDocLocation, xmlLocation);
    }

    @Test
    public void testRmFileExtension(){
        String s = FileUtil.rmSuffix("（2013）皖民四终字第00220号_ee7846c9-c0b3-49da-8b68-b49ab6119091判决书.txt");
    }

    @Test
    public void testGetFileName(){
        String s = FileUtil.getFileName("C:\\Users\\13051\\Desktop\\毕设\\data\\txt\\（2013）皖民四终字第00220号_ee7846c9-c0b3-49da-8b68-b49ab6119091判决书.txt");
    }

//    @Test
//    public void testSaveNewDocs(){
//      String s = saveService.saveNewDocs();
//    }
//
//    @Test
//    public void testSaveDocs() throws IOException, JSONException {
//        saveService.saveDocs(xmlLocation);
//    }

    @Test
    public void testDelete(){
        FileUtil.delAllFile(xmlLocation);
    }

    @Test
    public void testUnZip(){
        try {
            FileUtil.unZip(new File("C:\\Users\\13051\\Desktop\\毕设\\data\\test\\20150104.zip"),
                    "C:\\Users\\13051\\Desktop\\毕设\\data\\test\\");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUnRar(){
        try {
            FileUtil.unRar(new File("C:\\Users\\13051\\Desktop\\毕设\\data\\test\\20150104.rar"),
                    "C:\\Users\\13051\\Desktop\\毕设\\data\\test\\");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

