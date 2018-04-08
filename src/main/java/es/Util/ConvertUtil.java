package es.Util;

import com.google.gson.Gson;
import es.entity.esEntity.DocEntity;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by TYF on 2018/3/31.
 */
public class ConvertUtil {

    public static HashMap<String,String> xmlToMap(File file){
        HashMap<String,String> hashMap =new HashMap<String,String>();
        SAXReader reader = new SAXReader();
        try {
            // 通过reader对象的read方法加载xml文件,获取document对象。
            Document document = reader.read(file);
            // 通过document对象获取根节点root
            Element root = document.getRootElement();
            //遍历root下的属性
            List<Attribute> attributes = root.attributes();
            for (Attribute attr : attributes) {
                hashMap.put(AttrUtil.attrConvert(attr.getName()),simplyContent(attr.getValue()));
                //System.out.println("属性名：" + attr.getName() + "\n属性值：" + attr.getValue());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    public static JSONObject xmlToJson(File file){
        HashMap<String,String> hashMap=xmlToMap(file);
        JSONObject json = new JSONObject(hashMap);
        return json;
    }

    public static DocEntity xmlToEntity(File file){
        return jsonToEntity(xmlToJson(file));
    }

    public static DocEntity jsonToEntity(JSONObject json){
        Gson gson = new Gson();
        DocEntity doc = gson.fromJson(String.valueOf(json),DocEntity.class);
        return doc;
    }
    //删除正文中的换行符
    private static String simplyContent(String sourceString){
        return sourceString.replace("&#xA;","\n");
    }
}
