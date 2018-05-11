package es.service;

import es.entity.jpaEntity.UserEntity;
import org.json.JSONException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by TYF on 2018/2/26.
 */
public interface SaveService {

    boolean uploadDoc(MultipartFile multipartFile, UserEntity userEntity);

    boolean uploadDoc(List<MultipartFile> multipartFileList, UserEntity userEntity);

    boolean saveDoc(MultipartFile multipartFile, UserEntity userEntity);

    boolean saveDoc();

    boolean saveDoc(List<MultipartFile> multipartFileList, UserEntity userEntity);
}