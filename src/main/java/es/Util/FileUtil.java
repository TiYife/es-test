package es.Util;

import com.github.junrar.Archive;
import es.entity.jpaEntity.UpLogEntity;
import es.service.impl.SearchServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


import static es.Constant.ENCODING;
import static es.Constant.timeFormat;
import static es.Constant.suffixList;

/**
 * Created by TYF on 2018/5/8.
 */
public class FileUtil {

    /*日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);


    //列举出文件夹下所有的文件,生成list
    public static boolean listFile(File file, List<String> list) throws IOException {
        if (!file.exists()) return false;
        if (file.isDirectory()) {
            LOGGER.info("读取文件夹:" + file.getAbsolutePath());
            File[] fileList = file.listFiles();
            for (File sub : fileList
                    ) {
                listFile(sub, list);
            }
        } else {
            String location = file.getAbsolutePath();
            LOGGER.info("读取文件:" + location);
            list.add(location);
        }
        return true;
    }

    //获取文件名
    public static String getFileName(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            int slash = fileName.lastIndexOf('\\');
            if ((dot > -1) && (dot < (fileName.length()))
                    && (slash >= -1) && (slash < (fileName.length()))) {
                return fileName.substring(slash + 1, dot);
            }
        }
        return fileName;
    }

    //获取文件后缀
    public static String getSuffix(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot > -1) && (dot < (fileName.length()))) {
                return fileName.substring(dot + 1);
            }
        }
        return fileName;
    }

    //去除文件后缀
    public static String rmSuffix(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot > -1) && (dot < (fileName.length()))) {
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
            } else {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    public static void dirMappingMove(String fileDirectoryPath, String fileDirectoryPathHead, String saveAddress) {
        File dir = new File(fileDirectoryPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    dirMappingMove(files[i].getAbsolutePath(), fileDirectoryPathHead, saveAddress); // 获取文件绝对路径
                } else {
                    String strFileName = files[i].getAbsolutePath();
                    fileMappingMove(strFileName, fileDirectoryPathHead, saveAddress);
                }
            }
        }
        dir.delete();
    }

    public static String fileMappingMove(String fileAddress, String fileAddressHead, String saveAddress) {
        try {
            String saveFileAddress = "";
            File file = new File(fileAddress);
            if (fileAddress.indexOf(fileAddressHead) == 0) {
                saveFileAddress = fileAddress.replace('\\', '/').replaceAll(fileAddressHead.replace('\\', '/'), saveAddress.replace('\\', '/')).replace('/', '\\');
                String lastDir = saveFileAddress.substring(0, saveFileAddress.lastIndexOf("\\") + 1);
                File preFile = new File(lastDir);
                if (!preFile.exists())
                    preFile.mkdirs();
                file.renameTo(new File(saveFileAddress));
                return saveFileAddress;
            } else
                throw new Exception("存储地址错误");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static File uploadFile(@NotNull MultipartFile multipartFile, String saveLocation) {
        File file = new File(saveLocation);
        if (!file.exists()) {
            file.mkdirs();
        }

        String oldName = multipartFile.getOriginalFilename();
        if (!Objects.equals(oldName, "")) {
            UUID uuid = UUID.fromString(getFileName(oldName));
//            TODO IMPORTANT: 获取文件名后缀带点  example:   suffix='.docx'
            String suffix = oldName.substring(oldName.lastIndexOf("."));
            String newName = uuid.toString().replaceAll("-", "");
            String fileTotalName = newName + suffix;
            File isFile = new File(saveLocation + fileTotalName);
            try {
                multipartFile.transferTo(isFile);
                return isFile;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static List<File> uploadFile(@NotNull List<MultipartFile> files, String saveLocation) {

        List<File> list = new ArrayList<>();

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
                list.add(uploadFile(mf, saveLocation));
            }
        }
        return list;

    }

    public static void unZip(File zipFile, String desDir) throws Exception {

        ZipFile zip = new ZipFile(zipFile, Charset.forName(ENCODING));//解决中文文件夹乱码

        if (zip == null) {
            throw new Exception(zip.getName() + " NOT FOUND!");
        }


        String name = getFileName(zip.getName());
        File pathFile = new File(desDir + name);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }

        for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = entries.nextElement();
            String fileName = entry.getName();
            String outPath = (desDir + name + "/" + fileName).replaceAll("\\*", "/");
            File saveFile = new File(outPath);

            // 判断路径是否存在,不存在则创建文件路径
            File parent = new File(outPath.substring(0, outPath.lastIndexOf('\\')));
            if (!parent.exists()) {
                parent.mkdirs();
            }

            // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (saveFile.isDirectory()) {
                continue;
            }

            InputStream in = zip.getInputStream(entry);
            FileOutputStream out = new FileOutputStream(saveFile);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
        return;
    }

    public static void unRar(File rarFile, String desDir) throws Exception {

        Archive archive = new Archive(rarFile);
        if (archive == null) {
            throw new Exception(rarFile.getName() + " NOT FOUND!");
        }

        String totalName =  rarFile.getName();
        String name = getFileName(totalName);
        File pathFile = new File(desDir + name);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }

        List<com.github.junrar.rarfile.FileHeader> files = archive.getFileHeaders();
        for (com.github.junrar.rarfile.FileHeader fh : files) {
            String fileName = fh.getFileNameW();
            if (fileName != null && fileName.trim().length() > 0) {
                String outPath = (desDir + name + "/" + fileName).replaceAll("\\*", "/");
                File saveFile = new File(outPath);

                // 判断路径是否存在,不存在则创建文件路径
                File parent = new File(outPath.substring(0, outPath.lastIndexOf('\\')));
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (saveFile.isDirectory()) {
                    continue;
                }

                FileOutputStream fos = new FileOutputStream(saveFile);
                archive.extractFile(fh, fos);
                fos.flush();
                fos.close();
            }
        }
    }

}
