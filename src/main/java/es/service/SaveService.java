package es.service;

import es.entity.jpaEntity.OriDocEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by TYF on 2018/2/26.
 */
public interface SaveService {
    void uploadPackage(MultipartFile multipartFile, int userId);

    void uploadAndSave(MultipartFile multipartFile, int userId);

    boolean saveAllDoc();

    @Scheduled(cron = "0 30 0 * * ? ")
    //@Scheduled(cron = "0 0/5 * * * ? ")
    void autoSave();

    List<OriDocEntity> listDocs();

    void deleteDoc(String docId);

    void delete(List<String> docIds);
}