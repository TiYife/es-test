package es.service;

public interface WordSeparateService {
    public String getnn(String s);

    public String getN(String s);
    public String getKeyWord(String s);
    public int NLPTR_Init();
    public int NLPTR_Exit();

    String multiFileProcessAndSave(String fileDirectoryPath, String fileDirectoryPathHead, String fileDirectorySavePath);

    public String readToString(String fileName);

    void fileProcessAndSave(String fileAddress,String fileAddressHead, String saveAddress);
}
