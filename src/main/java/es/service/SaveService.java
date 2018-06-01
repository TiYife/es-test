package es.service;

import es.entity.jpaEntity.OriDocEntity;
import es.entity.jpaEntity.UpLogEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by TYF on 2018/2/26.
 */
public interface SaveService {

    String uploadFile(MultipartFile multipartFile, int userId);

    String saveFile(UpLogEntity upLogEntity);

    boolean saveAllDoc();

    List<OriDocEntity> listDocs();

    void deleteDoc(String docId);

    void deleteDoc(List<String> docIds);

    List<UpLogEntity> listUploading();

    List<UpLogEntity> listUploaded();

    void deleteUpLog(String id);

    void deleteUpLog(List<String> docIds);
}