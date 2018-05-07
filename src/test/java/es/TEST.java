package es;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jna.Native;
import es.entity.esEntity.DocEntity;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import javax.print.Doc;
import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

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

    @Test
    public void testSave(){
        File file = new File(Constant.FILE_LOCATION+"20140107\\安徽\\安徽省安庆市中级人民法院\\行政案件\\bbf10813-8274-48e7-b6af-56dc1b4a29c5.xml");
        saveService.saveDoc(file);
    }

    @Test
    public void testSaveDocs(){
        File file = new File(Constant.FILE_LOCATION+"20140107\\");
        saveService.saveDocs(file);
    }

    @Test
    public void testSearch(){
        List<DocEntity> docEntities=searchService.searchLaw(1,10,"content","被告人");
    }

    @Test
    public void testSimilarSearch(){
        List<DocEntity> docEntities = searchService.similarSearch(0,10,"宋皓以“其与黄某某是合作关系，借款与担保是两人之间约定的特殊合作方式，其参与了公司的日常经营管理活动；原判认定事实不清，适用法律错误”为由，向贵州省高级人民法院申诉，该院于2011年12月16日作出（2011）黔高调刑监字第25号通知驳回申诉。");
        assert docEntities.size()>0;
    }

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
    public void getnn()
    {
        //初始化
        NLPTRService instance = (NLPTRService) Native.loadLibrary(System.getProperty("user.dir") + "\\source\\NLPIR", NLPTRService.class);
        int init_flag = instance.NLPIR_Init("", 1, "0");
        String resultString = null;
        if (0 == init_flag) {
            resultString = instance.NLPIR_GetLastErrorMsg();
            System.err.println("初始化失败！\n" + resultString);
        }

        String sInput = "哎~那个金刚圈尺寸太差，前重后轻，左宽右窄，他戴上去很不舒服，整晚失眠会连累我嘛，他虽然是只猴子，但你也不能这样对他啊，官府知道会说我虐待动物的，说起那个金刚圈，啊~去年我在陈家村认识了一个铁匠，他手工精美，价钱又公道，童叟无欺，干脆我介绍你再定做一个吧！";
        //sInput = s;
        try {
            //resultString = instance.NLPIR_ParagraphProcess(sInput, 1);
            //System.out.println("分词结果为：\n " + resultString);

            String res="不觉明历";
            instance.NLPIR_AddUserWord(res);
            instance.NLPIR_SaveTheUsrDic();

            instance.NLPIR_Exit();

        } catch (Exception e) {
            System.out.println("错误信息：");
            e.printStackTrace();
        }
    }

    @Test
    public void testJson(){
        String json="{[\n" +
                "        {\n" +
                "            \"id\": 0,\n" +
                "            \"name\": \"Item 0\",\n" +
                "            \"price\": \"$0\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 1,\n" +
                "            \"name\": \"Item 1\",\n" +
                "            \"price\": \"$1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 2,\n" +
                "            \"name\": \"Item 2\",\n" +
                "            \"price\": \"$2\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 3,\n" +
                "            \"name\": \"Item 3\",\n" +
                "            \"price\": \"$3\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 4,\n" +
                "            \"name\": \"Item 4\",\n" +
                "            \"price\": \"$4\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 5,\n" +
                "            \"name\": \"Item 5\",\n" +
                "            \"price\": \"$5\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 6,\n" +
                "            \"name\": \"Item 6\",\n" +
                "            \"price\": \"$6\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 7,\n" +
                "            \"name\": \"Item 7\",\n" +
                "            \"price\": \"$7\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 8,\n" +
                "            \"name\": \"Item 8\",\n" +
                "            \"price\": \"$8\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 9,\n" +
                "            \"name\": \"Item 9\",\n" +
                "            \"price\": \"$9\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 10,\n" +
                "            \"name\": \"Item 10\",\n" +
                "            \"price\": \"$10\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        Type listType = new TypeToken<List<HashMap<String,String>>>() {}.getType();
        List<String> list = new Gson().fromJson(json, listType);
    }
}

