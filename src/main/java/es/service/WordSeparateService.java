package es.service;

import es.entity.esEntity.DocEntity;

import java.io.File;
import java.util.List;

public interface WordSeparateService {
    public String getnn(String s);

    public String getN(String s);
    public String getKeyWord(String s);
    public int NLPTR_Init();
    public int NLPTR_Exit();

    String multiFileProcessAndSave(String fileDirectoryPath, String fileDirectoryPathHead, String fileDirectorySavePath);

    public String readToString(String fileName);

    String fileProcessAndSave(String fileAddress, String fileAddressHead, String saveAddress);

    String getHFWordFormFiles(String caseType, List<DocEntity> docEntities)//获取高频词组 参数代表案件类型 all所有类型
    ;
}
