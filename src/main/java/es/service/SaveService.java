package es.service;

import es.entity.jpaEntity.OriDocEntity;
import es.entity.jpaEntity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by TYF on 2018/2/26.
 */
public interface SaveService {

    boolean uploadZip(MultipartFile multipartFile, int userId);

    boolean uploadRar(MultipartFile multipartFile, int userId);

    void uploadAndSave(MultipartFile multipartFile, int userId);

    boolean saveAllDoc();

    List<OriDocEntity> listDocs();

    void deleteDoc(String docId);

    void delete(List<String> docIds);
}