package es.service;

import es.entity.esEntity.DocEntity;

import java.util.List;

public interface WordSeparateService {
    public String getnn(String s);

    public String getN(String s);
    public String getKeyWord(String s);
    public int NLPTR_Init();
    public int NLPTR_Exit();

    String multiFileProcessAndSave(String fileDirectoryPath, String fileDirectoryPathHead, String fileDirectorySavePath);
    public void createPreDirectory(String fileAddress);

    public String readToString(String fileName);
    public String stringToRead(String sourceString,String fileAddress,boolean append);

    String fileProcessAndSave(String fileAddress, String fileAddressHead, String saveAddress);

    String getHFWordFormFiles(String caseType, List<DocEntity> docEntities)//获取高频词组 参数代表案件类型 all所有类型
    ;

    public int addDic(String word,String type);
    public int saveDic();
    public String getEncoding(String str);
    public String getUTF8StringFromGBKString(String gbkStr);
}
