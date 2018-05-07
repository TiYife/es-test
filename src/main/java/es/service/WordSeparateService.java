package es.service;

public interface WordSeparateService {
    public String getnn(String s);

    public String getN(String s);
    public String getKeyWord(String s);
    public int NLPTR_Init();
    public int NLPTR_Exit();

    public String readToString(String fileName);
}
