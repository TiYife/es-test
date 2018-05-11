package es.Util;

import es.entity.jpaEntity.OriDocEntity;
import es.entity.jpaEntity.UserEntity;
import es.service.impl.SearchServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.*;

import static es.Constant.dataFormat;
import static es.Constant.suffixList;

/**
 * Created by TYF on 2018/5/8.
 */
public class FileUtil {

    /*日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

    //列举出文件夹下所有的文件,生成list
    public static boolean listFile(File file , List<String> list) throws IOException {
        if(!file.exists())return false;
        if(file.isDirectory()) {
            LOGGER.info("读取文件夹:"+file.getAbsolutePath());
            File[] fileList = file.listFiles();
            for (File sub:fileList
                    ) {
                listFile(sub,list);
            }
        }else{
            String location=file.getAbsolutePath();
            LOGGER.info("读取文件:"+location);
            list.add(location);
        }
        return true;
    }

   //获取文件名
    public static String getFileName(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            int slash =fileName.lastIndexOf('\\');
            if ((dot >-1) && (dot < (fileName.length()))
                    &&(slash >-1) && (slash < (fileName.length()))) {
                return fileName.substring(slash+1, dot);
            }
        }
        return fileName;
    }

    //获取文件后缀
    public static String getSuffix(String fileName){
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot >-1) && (dot < (fileName.length()))) {
                return fileName.substring(dot+1);
            }
        }
        return fileName;
    }

    //去除文件后缀
    public static String rmSuffix(String fileName){
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot >-1) && (dot < (fileName.length()))) {
                return fileName.substring(0, dot);
            }
        }
        return fileName;
    }

    //删除文件夹
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除指定文件夹下所有文件
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    public static void dirMappingMove(String fileDirectoryPath,String fileDirectoryPathHead,String saveAddress){
        File dir = new File(fileDirectoryPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    dirMappingMove(files[i].getAbsolutePath(),fileDirectoryPathHead,saveAddress); // 获取文件绝对路径
                } else {
                    String strFileName = files[i].getAbsolutePath();
                    fileMappingMove(strFileName,fileDirectoryPathHead,saveAddress);
                }
            }
        }
        dir.delete();
    }

    public static void fileMappingMove(String fileAddress, String fileAddressHead, String saveAddress)
    {
        try {
            String saveFileAddress="";
            File file = new File(fileAddress);
            if(fileAddress.indexOf(fileAddressHead)==0)
            {
                saveFileAddress= fileAddress.replace('\\','/').replaceAll(fileAddressHead.replace('\\','/'),saveAddress.replace('\\','/')).replace('/','\\');
                String lastDir = saveFileAddress.substring(0, saveFileAddress.lastIndexOf("\\") + 1);
                File preFile = new File(lastDir);
                if (!preFile.exists())
                    preFile.mkdirs();
                file.renameTo(new File(saveFileAddress));
            }
            else
                throw new Exception( "存储地址错误");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public  static OriDocEntity uploadFile(@NotNull MultipartFile multipartFile, String saveLocation, int userId)
            throws IOException {
        String filename = multipartFile.getOriginalFilename();
        if (!Objects.equals(filename, "")) {
            UUID uuid = UUID.randomUUID();
//            TODO IMPORTANT: 获取文件名后缀带点  example:   suffix='.docx'
            String suffix = filename.substring(filename.lastIndexOf("."));
            String fileName = uuid.toString().replaceAll("-", "");
            String fileTotalName = fileName + suffix;
            File isFile = new File(saveLocation + fileTotalName);
            try {
                multipartFile.transferTo(isFile);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            OriDocEntity entity = new OriDocEntity();
            entity.setId(fileName);
            entity.setLocation(saveLocation + fileTotalName);
            entity.setUpTime(dataFormat.format(new Date()));
            entity.setUploader(userId);

            return entity;

        }
        return null;
    }

    public static List<OriDocEntity> uploadFile(@NotNull List<MultipartFile> files, String saveLocation, int userId) throws IOException {

        List<OriDocEntity> list = new ArrayList<>();

        //文件格式要求
        String[] suffixArr = suffixList;
        List<String> suffixList = Arrays.asList(suffixArr);

        //判断存储的文件夹是否存在
        File file = new File(saveLocation);
        if (!file.exists()) {
            file.mkdirs();
        }

        //遍历文件夹
        for (MultipartFile mf : files) {
            if (!mf.isEmpty()) {
                String originalFilename = mf.getOriginalFilename();
                String suffix = getSuffix(originalFilename);
                //格式限制，非wav格式的不上传
                if (!suffixList.contains(suffix)) {
                    continue;
                }
                list.add(uploadFile(mf, saveLocation, userId));
            }
        }
        return list;

    }

}
