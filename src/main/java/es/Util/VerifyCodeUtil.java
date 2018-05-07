package es.Util;

import java.util.Random;

public class VerifyCodeUtil {
    static String letterArray = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generateCode()
    {
        char[] code = {'1','1','1','1'};
        Random random = new Random();
        for(int i=0;i<4;i++)
        {
            code[i]=letterArray.charAt(Math.abs(random.nextInt())%62);
        }
        String showCode = new String(code);
        return showCode;
    }
}
