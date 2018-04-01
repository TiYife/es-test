package es.Util;

/**
 * Created by TYF on 2018/4/1.
 */
public class AttrUtil {

    public static String attrConvert(String attr){
        switch (attr){
            case "文书ID":
                return "docId";
            case "案件名称":
                return "caseName";
            case "法院名称":
                return "courtName";
            case "法院层级":
                return "courtLevel";
            case "法院省份":
                return "courtProvince";
            case "法院地市":
                return "courtCity";
            case "法院区县":
                return "courtCountry";
            case "法院区域":
                return "courtRegion";
            case "法院试点":
                return "isExpUnit";
            case "法院类型":
                return "courtType";
            case "案件类型":
                return "caseType";
            case "审判程序":
                return "trialProcedure";
            case "文书类型":
                return "docType";
            case "案号":
                return "caseNo";
            case "案由":
                return "caseCause";
            case "当事人":
                return "client";
            case "审判人员":
                return "judge";
            case "书记员":
                return "clerk";
            case "裁判月份":
                return "trialMonth";
            case "裁判日期":
                return "trialDate";
            case "裁判年份":
                return "trialYear";
            case "一级法院":
                return "courtLevelOne";
            case "二级法院":
                return "courtLevelTwo";
            case "三级法院":
                return "courtLevelThree";
            case "四级法院":
                return "courtLevelFour";
            case "上级法院名称":
                return "courtSuperior";
            case "DocContent":
                return "content";
            case "法院ID":
                return "courtId";
            default:
                return "error";
        }
    }
}
