package es;

import java.text.SimpleDateFormat;

/**
 * Created by TYF on 2018/2/26.
 */
public class Constant {
    //public static final String INDEX_NAME="es_web";
    public static final String INDEX_NAME="es";

    //文件格式相关
    public static final String ENCODING="GBK";
    public static final String[] suffixList = {"txt"};

    //时间格式
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    //文件位置相关
    public static final String FILE_LOCATION="E:\\系统测试\\data\\";
    public static String originalDocLocation=FILE_LOCATION+"txt\\";
    public static String xmlLocation=FILE_LOCATION+"xml\\";
    public static String newDocLocation=FILE_LOCATION+"new\\";
    public static String dicFileLocation=FILE_LOCATION+"dic\\";



    public static final String HFWord_PATH="E:\\桌面存放\\测试\\全局高频词组.txt";

    public enum WordType {PREFIX,
        PREP,
        ECHO,
        EXPR,
        SUFFIX,
        PUNC,
        N,
        ADV,
        CLAS,
        COOR,
        CONJ,
        V,
        STRU,
        PP,
        P,
        ADJ,
        PRON,
        AUX,
        NUM
    }

}
