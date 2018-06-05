package es.service;

import es.entity.jpaEntity.TxtEntity;
import es.entity.jpaEntity.UpLogEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by TYF on 2018/2/26.
 */
public interface SaveService {

    String uploadFile(MultipartFile multipartFile, int userId);

    String saveFile(UpLogEntity upLogEntity);

    String saveAll();

    List<TxtEntity> listDocs();

    String deleteTxt(String docId);

    void deleteTxt(List<String> docIds);

    List<UpLogEntity> listUpLog();

    String deleteUpLog(String id);

    void deleteUpLog(List<String> docIds);
}