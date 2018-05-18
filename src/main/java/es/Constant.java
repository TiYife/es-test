package es;

/**
 * Created by TYF on 2018/2/26.
 */
public class Constant {
    public static final String INDEX_NAME="es_web";
    public static final String ENCODING="GBK";

    public static final String FILE_LOCATION="C:\\Users\\13051\\Desktop\\毕设\\data\\";
    public static String originalDocLocation=FILE_LOCATION;
    public static String xmlLocation=FILE_LOCATION;
    public static String newDocLocation=FILE_LOCATION;

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
        NUM;
    }

}
