package es.service.impl;

import com.sun.jna.Native;
import es.service.WordSeparateService;
import es.service.NLPTRService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordSeparateServiceImpl implements WordSeparateService {
    public String getnn(String s)
    {
        //初始化
        NLPTRService instance = (NLPTRService) Native.loadLibrary(System.getProperty("user.dir") + "\\source\\NLPIR", NLPTRService.class);
        int init_flag = instance.NLPIR_Init("", 1, "0");
        String resultString = null;
        if (0 == init_flag) {
            resultString = instance.NLPIR_GetLastErrorMsg();
            System.err.println("初始化失败！\n" + resultString);
            return "";
        }

        String sInput = "哎~那个金刚圈尺寸太差，前重后轻，左宽右窄，他戴上去很不舒服，整晚失眠会连累我嘛，他虽然是只猴子，但你也不能这样对他啊，官府知道会说我虐待动物的，说起那个金刚圈，啊~去年我在陈家村认识了一个铁匠，他手工精美，价钱又公道，童叟无欺，干脆我介绍你再定做一个吧！";
        sInput = s;
        try {
            resultString = instance.NLPIR_ParagraphProcess(sInput, 1);
            //System.out.println("分词结果为：\n " + resultString);

            String res="";
            res=getN(resultString);

            instance.NLPIR_Exit();
            return res;

        } catch (Exception e) {
            System.out.println("错误信息：");
            e.printStackTrace();
            return "";
        }
    }

    public String getN(String s){
        Pattern pattern = Pattern.compile("/[a-z]+ ([^ ]+?)/[nv]");
        Matcher matcher = pattern.matcher(s);
        String re="";
        while (matcher.find())
        {
            re=re+matcher.group(1)+" \n";
        }
        return re;
    }
}
