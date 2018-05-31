package es.service.impl;

import com.sun.jna.Native;
import es.Constant;
import es.Util.FileUtil;
import es.entity.esEntity.DocEntity;
import es.entity.wordSepa.wordSepaEnity;
import es.repository.esRepository.DocRepository;
import es.service.NLPTRService;
import es.service.WordSeparateService;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static es.Constant.HFWord_PATH;

@Service
public class WordSeparateServiceImpl implements WordSeparateService {

    @Autowired
    DocRepository docRepository;

    public NLPTRService instance =(NLPTRService) Native.loadLibrary(System.getProperty("user.dir") + "\\source\\NLPIR", NLPTRService.class);

    private String FILE_PATH=Constant.xmlLocation;



    @Override
    public String getHFWordFormFiles(String caseType,List<DocEntity> docEntities)//获取高频词组 参数代表案件类型 all所有类型
    {
        List<DocEntity> list = docEntities;
        try {

            //list=docRepository.findAll();
            //list=searchService.allLaw();
            //List<DocEntity> list=searchService.searchLaw(0,10,"","");
            /*if (caseType == "all") {
                //list = searchService.
            } else {
                list=searchService.allLaw();

            }*/
            int size=list.size();
            int a = 20,b=100,d=30;
            double  c = 0.5;
            //if (NLPTR_Init() != 1) throw new Exception("分词程序初始化失败");
            Map<String, Integer> Word = new HashMap<String, Integer>();


            NLPTRService instance1 = (NLPTRService) Native.loadLibrary(System.getProperty("user.dir") + "\\source\\NLPIR", NLPTRService.class);
            int init_flag = instance1.NLPIR_Init("", 1, "0");
            String resultString = null;
            if (0 == init_flag) {
                resultString = instance1.NLPIR_GetLastErrorMsg();
                System.err.println("初始化失败！\n" + resultString);
                return "";
            }
            int k=0;
            for(DocEntity doc :list)
            {
                k++;
                String docAfterProcess = instance1.NLPIR_ParagraphProcess(doc.getContent(),0);
                String[] wlist=docAfterProcess.split(" ");
                for(String s:wlist)
                {
                    if(s.equals("")) continue;
                    if(Word.containsKey(s))
                        Word.put(s,Word.get(s)+1);
                    else
                        Word.put(s,1);
                    for(int i=0;i<wlist.length;i++)
                        if(wlist[i].equals(s))
                            wlist[i]="";
                }
                List<String> wordToDeleteList=new ArrayList<String>();

                for (String w : Word.keySet()) {
                    //map.keySet()返回的是所有key的值
                    int f=Word.get(w);
                    if(k>a&&k<b&&(double)f/(double)k<c) wordToDeleteList.add(w);
                    if(k>b&&f<d) wordToDeleteList.add(w);
                }
                for(int i=0;i<wordToDeleteList.size();i++)
                    Word.remove(wordToDeleteList.get(i));
            }
            String result="";
            String sp="";
            for (String w : Word.keySet()) {
                result+=sp+w+"\t"+Word.get(w).toString();
                sp="\n";
            }
            stringToRead(result,HFWord_PATH,false);
            return "success";
        }catch (Exception e)
        {
            return e.getMessage();
        }
    }

    public int NLPTR_Init(){
        int init_flag = instance.NLPIR_Init("", 1, "0");
        String resultString = null;
        if (0 == init_flag) {
            resultString = instance.NLPIR_GetLastErrorMsg();
            System.err.println("初始化失败！\n" + resultString);
            return 0;
        }
        return 1;
    }

    public int NLPTR_Exit(){
        try{
            instance.NLPIR_Exit();
        }catch (Exception e) {
            System.out.println("错误信息：");
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

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

    public Double getNLPIR_FileProcess(String sourceFileAddress,String targetFileAddress)
    {
        try {
            Double result =instance.NLPIR_FileProcess(sourceFileAddress,targetFileAddress,1);

            return result;

        } catch (Exception e) {
            System.out.println("错误信息：");
            e.printStackTrace();
            return -1.0;
        }
    }

    public String getKeyWord(String sInput)
    {
        try {
            //resultString = instance.NLPIR_ParagraphProcess(sInput, 1);
            //System.out.println("分词结果为：\n " + resultString);
            String resultString =instance.NLPIR_GetKeyWords(sInput,50,true);

            //String[] lines = ii.split("\r");

            return resultString;

        } catch (Exception e) {
            System.out.println("错误信息：");
            e.printStackTrace();
            return "error";
        }
    }

    public String filePreProcess(String fileString)
    {
        return fileString.replaceAll("(\r\n)+\t*","\r\n")
                .replaceAll(" ","")
                .replaceAll("([^　])　(?!　)","$1")
                .replaceAll("(</?[a-z0-9]+?>)+\r\n","");
    }

    public void testA(String fileAddress,wordSepaEnity wEnity)
    {
        String re=fileAddress+"\t\t"+(wEnity.docId==null?"文书ID无":"文书ID")
                +"\t\t"
                +(wEnity.caseName==null?"案件名称无":"案件名称")
                +"\t\t"
                +(wEnity.courtName==null?"法院名称无":"法院名称")
                +"\t\t"
                +(wEnity.courtProvince==null?"法院省份无":"法院省份")
                +"\t\t"
                +(wEnity.courtCountry==null?"法院区县无":"法院区县")
                +"\t\t"
                +(wEnity.courtRegion==null?"法院区域无":"法院区域")
                +"\t\t"
                +(wEnity.caseType==null?"案件类型无":"案件类型")
                +"\t\t"
                +(wEnity.trialProcedure==null?"审判程序无":"审判程序")
                +"\t\t"
                +(wEnity.docType==null?"文书类型无":"文书类型")
                +"\t\t"
                +(wEnity.caseNo==null?"案号无":"案号")
                +"\t\t"
                +(wEnity.caseCause==null?"案由无":"案由")
                +"\t\t"
                +(wEnity.client==null?"当事人无":"当事人")
                +"\t\t"
                +(wEnity.judge==null?"审判人员无":"审判人员")
                +"\t\t"
                +(wEnity.clerk==null?"书记员无":"书记员")
                +"\t\t"
                +(wEnity.trialDate==null?"裁判月份无":"裁判月份")
                +"\t\t"
                +(wEnity.trialMonth==null?"裁判日期无":"裁判日期")
                +"\t\t"
                +(wEnity.trialYear==null?"裁判年份无":"裁判年份")
                +"\t\t"
                +(wEnity.content==null?"DocContent无":"DocContent")
                +"\t\t"
                +(wEnity.courtId==null?"法院ID无":"法院ID")
                +"\r\n";


        stringToRead(re,"E:\\桌面存放\\测试\\errorTest.txt",true);//ceshi

    }

    public File fileProcessAndSave(String fileAddress, String fileAddressHead, String saveAddress)
    {
        try {
            if(NLPTR_Init()!=1) throw new Exception( "分词程序初始化失败");
            String fileString=filePreProcess(readToString(fileAddress));
            if(fileString==""||fileString==null) throw new Exception( "文件读取失败");
            String[] fileLines=fileString.split("\r\n");
            String[] fileLinesAfterProcess=instance.NLPIR_ParagraphProcess(fileString,1).split("\r");

            wordSepaEnity wordSepaEnity1=new wordSepaEnity();
            int fileError=0;
            int processStep=0;
            String dsrresult="";//当事人
            String docContent="";
            String sprresult="";//审判人
            String ayResult ="";//案由
            String errorDetail="";
            for(int i=0;i<fileLines.length;i++)
            {
                String content=fileLines[i].trim();
                if(content.equals(""))
                    continue;
                switch (processStep)
                {
                    case 0:
                        Pattern pattern2 = Pattern.compile("^.{10,}_([0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12})(.{3,6}?)\\.txt$");
                        Matcher matcher = pattern2.matcher(fileAddress);
                        while (matcher.find()) {
                            String docId=matcher.group(1);
                            if(!docId.equals(""))
                                wordSepaEnity1.docId=docId;
                            else
                                errorDetail+="\n"+String.valueOf(i+1)+"\t无法获取docId";
                            String docType=matcher.group(2);
                            if(docType!=""&&docType.endsWith("书"))
                                wordSepaEnity1.docType=docType;
                            else
                                errorDetail+="\n"+String.valueOf(i+1)+"\t无法获取docType";
                        }
                        processStep=1;
                        i--;
                        break;
                    case 1:
                        if(Pattern.matches("^.+书([0-9]+号)?$", fileLines[i])){
                            wordSepaEnity1.caseName = content;
                            if (fileLinesAfterProcess[i].contains("/ay")) {
                                ayResult += getWordByType(fileLinesAfterProcess[i], "ay");
                            }
                        }
                        else
                        {
                            fileError=1;
                            errorDetail+="\n"+String.valueOf(i+1)+"\t无法获取案例名称";
                            //TODO 加日志
                        }
                        processStep=2;
                        break;
                    case 2:
                        if(content.endsWith("法院")) {
                            wordSepaEnity1.courtName = content;
                            if(fileLinesAfterProcess[i].contains("/ns"))
                            {
                                wordSepaEnity1.courtCountry=getWordByType(fileLinesAfterProcess[i],"ns");
                                wordSepaEnity1.courtProvince=wordSepaEnity1.courtCountry.split(";")[0];
                            }
                            else
                            {
                                errorDetail+="\n"+String.valueOf(i+1)+"\t无法获取案例地区信息";
                            }
                        }
                        else
                        {
                            fileError=1;
                            errorDetail+="\n"+String.valueOf(i+1)+"\t无法获取法院名称";
                            //TODO 加日志
                        }
                        processStep=3;
                        break;
                    case 3:
                        if(content.endsWith("书"))
                            wordSepaEnity1.docType=content;
                        else
                        {
                            fileError=1;
                            errorDetail+="\n"+String.valueOf(i+1)+"\t无法获取文书类型";
                            //TODO 加日志
                        }
                        processStep=4;
                        break;
                    case 4:
                        if(content.endsWith("号"))
                            wordSepaEnity1.caseNo=content;
                        else
                        {
                            fileError=1;
                            errorDetail+="\n"+String.valueOf(i+1)+"\t无法获取案号";
                            //TODO 加日志
                        }
                        processStep=5;
                        break;
                    case 5:
                        String pattern = "^(.{1,6}?)/(dsr|dlr)(.+?)：?(((.+?)/nr(.+?)，(.+?))|(.+?))。(.+)$";//TODO 缺/ds
                        if(Pattern.matches(pattern, fileLinesAfterProcess[i])) {
                            if (fileLinesAfterProcess[i].contains("/dsr")) {
                                if (fileLinesAfterProcess[i].contains("/nr"))
                                    dsrresult += getWordByType(fileLinesAfterProcess[i], "nr");
                                else {
                                    Pattern pattern3 = Pattern.compile("^(?:.+?)/dsr(?:.+?)：(.+?)。(?:.+)$");
                                    Matcher matcher1 = pattern3.matcher(fileLinesAfterProcess[i]);
                                    while (matcher1.find()) {
                                        dsrresult += matcher1.group(1).replaceAll("/[a-z]+? ","") + ";";
                                    }
                                }

                            }
                            wordSepaEnity1.client = dsrresult;
                            processStep=5;
                        }
                        else
                        {
                            if(dsrresult.equals(""))
                            {
                                fileError=1;
                                errorDetail+="\n"+String.valueOf(i+1)+"\t无法获取当事人";
                                //TODO 加日志
                            }
                            else
                            {
                                processStep=6;
                                i--;
                            }
                        }
                        break;
                    case 6:
                        String pattern3 = "^(.+?)/spr[\\s\\t]*?(.+?)/nr(.+?)$";//TODO 缺/spr
                        if(!Pattern.matches(pattern3, fileLinesAfterProcess[i]))//TODO 可以多加一个判决结果
                        {
                            docContent+=content;
                            wordSepaEnity1.content=docContent;

                            if (fileLinesAfterProcess[i].contains("/dsr"))//获取案由
                                ayResult+=getWordByType(fileLinesAfterProcess[i],"ay");
                            processStep=6;
                        }
                        else
                        {
                            if(docContent.equals(""))
                            {
                                fileError=1;
                                errorDetail+="\n"+String.valueOf(i+1)+"\t无法获取判决内容";
                                //TODO 加日志
                            }
                            else
                            {
                                processStep=7;
                                i--;
                            }
                        }
                        break;
                    case 7:
                        String pattern4 = "^(.+?)/spr[\\s\\t]*?(.+?)/nr(.+?)$";//TODO 缺/spr
                        if(Pattern.matches(pattern4, fileLinesAfterProcess[i]))
                        {
                            sprresult += getWordByType(fileLinesAfterProcess[i],"nr") ;
                            wordSepaEnity1.judge = sprresult;
                            processStep=7;
                        }
                        else
                        {
                            if(docContent.equals(""))
                            {
                                fileError=1;
                                errorDetail+="\n"+String.valueOf(i+1)+"\t无法获取审判人员";
                                //TODO 加日志
                            }
                            else
                            {
                                processStep=8;
                                i--;
                            }
                        }
                        break;
                    case 8:
                        String pattern5 = "^(.+?)/t (.+?)/t (.+?)/t $";
                        if(Pattern.matches(pattern5, fileLinesAfterProcess[i]))
                        {
                            Pattern pattern6 = Pattern.compile("^(.+?)/t (.+?)/t (.+?)/t $");
                            Matcher matcher3 = pattern6.matcher(fileLinesAfterProcess[i]);
                            while (matcher3.find()) {
                                wordSepaEnity1.trialYear=matcher3.group(1);//TODO 日期转数字
                                wordSepaEnity1.trialMonth=matcher3.group(2);
                                wordSepaEnity1.trialDate=content;
                            }
                            processStep=9;
                        }
                        else
                        {
                            fileError=1;
                            errorDetail+="\n"+String.valueOf(i+1)+"\t无法获取审判日期";
                            //TODO 加日志
                        }
                        break;
                    case 9:
                        String pattern7 = "^(.+?)/sjy[\\s\\t]*?(.+?)/nr(.+?)$";//TODO 缺/sjr
                        if(Pattern.matches(pattern7, fileLinesAfterProcess[i]))
                        {
                            wordSepaEnity1.clerk = getWordByType(fileLinesAfterProcess[i],"nr");
                            processStep=10;
                        }
                        else
                        {
                            fileError=1;
                            errorDetail+="\n"+String.valueOf(i+1)+"\t无法获取书记员";
                            //TODO 加日志
                        }
                        break;

                    default:
                        fileError=1;
                        errorDetail+="\n"+String.valueOf(i+1)+"未知错误";
                        //TODO 加日志
                        break;

                }
            }
            //TODO 继续添加其他属性
            if(ayResult.equals(""))
                errorDetail+="\n无法获取案由";

            stringToRead(errorDetail,fileAddress+".error.etxt",false);

            String saveFileAddress="";
            if(fileAddress.indexOf(fileAddressHead)==0)
            {
                saveFileAddress= fileAddress.replace('\\','/').replaceAll(fileAddressHead.replace('\\','/'),saveAddress.replace('\\','/')).replace('/','\\');
            }
            else
                throw new Exception( "存储地址错误");
            testA(fileAddress,wordSepaEnity1);
            wordSepaEnityToXMLAndSave(wordSepaEnity1, FileUtil.rmSuffix(saveFileAddress)+".xml");
            return new File(FileUtil.rmSuffix(saveFileAddress)+".xml");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //System.out.println(line);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String multiFileProcessAndSave(String fileDirectoryPath,String fileDirectoryPathHead,String fileDirectorySavePath) {//TODO 还得从目录中获取信息
        File dir = new File(fileDirectoryPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    multiFileProcessAndSave(files[i].getAbsolutePath(),fileDirectoryPathHead,fileDirectorySavePath); // 获取文件绝对路径
                } else if (fileName.endsWith(".txt")) { //TODO 判断是不是要处理的文件名格式
                    String strFileName = files[i].getAbsolutePath();
                    fileProcessAndSave(strFileName,fileDirectoryPathHead,fileDirectorySavePath);
                } else {
                    continue;
                }
            }

        }
        return "success";
    }

    public int isWordByType(String sourceString,String type)
    {
        Pattern pattern = Pattern.compile("/[a-z]+ ([^ ]+?)/"+type);
        Matcher matcher = pattern.matcher(sourceString);
        return matcher.groupCount();
    }

    public String getWordByType(String sourceString,String type)
    {
        Pattern pattern = Pattern.compile("(?:/[a-z]+(?: |　)+?|^)([^ ]+?)/"+type);
        Matcher matcher = pattern.matcher(sourceString);
        String re="";
        String dot=";";
        while (matcher.find())
        {
            re+=dot+matcher.group(1);
            dot=";";
        }
        return re;
    }

    public void wordSepaEnityToXMLAndSave(wordSepaEnity wEnity,String saveAddress)throws Exception
    {
        org.jdom2.Document document=new org.jdom2.Document();
        org.jdom2.Element root=new org.jdom2.Element("案");
        root.setAttribute("文书ID", wEnity.docId==null ? "" : wEnity.docId.toString());
        root.setAttribute("案件名称", wEnity.caseName==null ? "" : wEnity.caseName.toString());
        root.setAttribute("法院名称", wEnity.courtName==null ? "" : wEnity.courtName.toString());
        root.setAttribute("法院省份", wEnity.courtProvince==null ? "" : wEnity.courtProvince.toString());
        root.setAttribute("法院区县", wEnity.courtCountry==null ? "" : wEnity.courtCountry.toString());
        root.setAttribute("法院区域", wEnity.courtRegion==null ? "" : wEnity.courtRegion.toString());
        root.setAttribute("案件类型", wEnity.caseType==null ? "" : wEnity.caseType.toString());
        root.setAttribute("审判程序", wEnity.trialProcedure==null ? "" : wEnity.trialProcedure.toString());
        root.setAttribute("文书类型", wEnity.docType==null ? "" : wEnity.docType.toString());
        root.setAttribute("案号", wEnity.caseNo==null ? "" : wEnity.caseNo.toString());
        root.setAttribute("案由", wEnity.caseCause==null ? "" : wEnity.caseCause.toString());
        root.setAttribute("当事人", wEnity.client==null ? "" : wEnity.client.toString());
        root.setAttribute("审判人员", wEnity.judge==null ? "" : wEnity.judge.toString());
        root.setAttribute("书记员", wEnity.clerk==null ? "" : wEnity.clerk.toString());
        root.setAttribute("裁判月份", wEnity.trialDate==null ? "" : wEnity.trialDate.toString());
        root.setAttribute("裁判日期", wEnity.trialMonth==null ? "" : wEnity.trialMonth.toString());
        root.setAttribute("裁判年份", wEnity.trialYear==null ? "" : wEnity.trialYear.toString());
        root.setAttribute("DocContent", wEnity.content==null ? "" : wEnity.content.toString());
        root.setAttribute("法院ID", wEnity.courtId==null ? "" : wEnity.courtId.toString());
        document.setRootElement(root);
        XMLOutputter outputter=new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());//设置文本格式
        createPreDirectory(saveAddress);
        outputter.output(document, new FileOutputStream(saveAddress));
    }

    public void createPreDirectory(String fileAddress){
        String lastDir = fileAddress.substring(0, fileAddress.lastIndexOf("\\") + 1);
        File file = new File(lastDir);
        if (!file.exists())
            file.mkdirs();
    }

    public String readToString(String fileName) {
        String encoding = Constant.ENCODING;
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    public String stringToRead(String sourceString,String fileAddress,boolean append){
        try {


            File file = new File(fileAddress);
            if (!file.exists())
                file.createNewFile();
            FileOutputStream out = new FileOutputStream(file, append);
            StringBuffer sb = new StringBuffer();
            sb.append(sourceString);
            out.write(sb.toString().getBytes("gbk"));
            out.close();
            return "success";
        }catch (Exception e)
        {
            return e.getMessage();
        }
    }

    /*public String getHFWordFormFiles(String caseType)//获取高频词组 参数代表案件类型 all所有类型
    {
        try {
            SearchService searchService = new SearchServiceImpl();
            List<DocEntity> list;
            //list=docRepository.findAll();
            list=searchService.allLaw();
            //List<DocEntity> list=searchService.searchLaw(0,10,"","");
            if (caseType == "all") {
                //list = searchService.
            } else {
                list=searchService.allLaw();

            }
            int size=list.size();
            int a = 20,b=100,d=30;
            double  c = 0.5;
            if (NLPTR_Init() != 1) throw new Exception("分词程序初始化失败");
            Map<String, Integer> Word = new HashMap<String, Integer>();

            int k=0;
            for(DocEntity doc :list)
            {
                k++;
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
            stringToRead(result,HFWord_PATH,false);
            return "success";
        }catch (Exception e)
        {
            return e.getMessage();
        }
    }*/
}
