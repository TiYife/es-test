package es.service.impl;

import com.sun.jna.Native;
import es.Constant;
import es.entity.wordSepa.wordSepaEnity;
import es.service.NLPTRService;
import es.service.WordSeparateService;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WordSeparateServiceImpl implements WordSeparateService {

    public NLPTRService instance =(NLPTRService) Native.loadLibrary(System.getProperty("user.dir") + "\\source\\NLPIR", NLPTRService.class);

    private String FILE_PATH=Constant.xmlLocation;

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
        return fileString.replaceAll("(\r\n)+\t*","\r\n").replaceAll(" ","").replaceAll("([^　])　(?!　)","$1");
    }

    public void fileProcessAndSave(String fileAddress,String fileAddressHead, String saveAddress)
    {
        try {
            if(NLPTR_Init()!=1) throw new Exception( "分词程序初始化失败");
            String fileString=filePreProcess(readToString(fileAddress));
            if(fileString==""||fileString==null) throw new Exception( "文件读取失败");
            String[] fileLines=fileString.split("\r\n");
            String[] fileLinesAfterProcess=instance.NLPIR_ParagraphProcess(fileString,1).split("\r");

            wordSepaEnity wordSepaEnity1=new wordSepaEnity();
            int fileError=0;
            int processStep=1;
            String dsrresult="";
            String docContent="";
            String sprresult="";
            String errorDetail="";
            for(int i=0;i<fileLines.length;i++)
            {
                String content=fileLines[i].trim();
                if(content.equals(""))
                    continue;
                switch (processStep)
                {
                    case 1:
                        if(content.endsWith("书"))
                            wordSepaEnity1.caseName=content;
                        else
                        {
                            fileError=1;
                            errorDetail+="\n"+String.valueOf(i+1)+"\t无法获取案例名称";
                            //TODO 加日志
                        }
                        processStep=2;
                        break;
                    case 2:
                        if(content.endsWith("法院"))
                            wordSepaEnity1.courtName=content;
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
                                    Pattern pattern2 = Pattern.compile("^(?:.+?)/dsr(?:.+?)：(.+?)。(?:.+)$");
                                    Matcher matcher = pattern2.matcher(fileLinesAfterProcess[i]);
                                    while (matcher.find()) {
                                        dsrresult += matcher.group(1).replaceAll("/[a-z]+? ","") + ";";
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
                            Matcher matcher = pattern6.matcher(fileLinesAfterProcess[i]);
                            while (matcher.find()) {
                                wordSepaEnity1.trialYear=matcher.group(1);//TODO 日期转数字
                                wordSepaEnity1.trialMonth=matcher.group(2);
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
            
            stringToRead(errorDetail,fileAddress+".error.txt");

            String saveFileAddress="";
            if(fileAddress.indexOf(fileAddressHead)==0)
            {
                saveFileAddress= fileAddress.replace('\\','/').replaceAll(fileAddressHead.replace('\\','/'),saveAddress.replace('\\','/')).replace('/','\\');
            }
            else
                throw new Exception( "存储地址错误");
            wordSepaEnityToXMLAndSave(wordSepaEnity1,saveFileAddress+".xml");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            //System.out.println(line);
            e.printStackTrace();
        }
    }

    public String multiFileProcessAndSave(String fileDirectoryPath,String fileDirectoryPathHead) {//TODO 还得从目录中获取信息
        File dir = new File(fileDirectoryPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    multiFileProcessAndSave(files[i].getAbsolutePath(),fileDirectoryPathHead); // 获取文件绝对路径
                } else if (fileName.endsWith("txt")) { //TODO 判断是不是要处理的文件名格式
                    String strFileName = files[i].getAbsolutePath();
                    fileProcessAndSave(strFileName,fileDirectoryPathHead,FILE_PATH);
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
        Pattern pattern = Pattern.compile("/[a-z]+(?: |　)+?([^ ]+?)/"+type);
        Matcher matcher = pattern.matcher(sourceString);
        String re="";
        while (matcher.find())
        {
            re=re+matcher.group(1)+";";
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

    public String stringToRead(String sourceString,String fileAddress){
        try {


            File file = new File(fileAddress);
            if (!file.exists())
                file.createNewFile();
            FileOutputStream out = new FileOutputStream(file, true);
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
}
