package es.service;

import es.entity.esEntity.DocEntity;
import es.entity.jpaEntity.FavoriteEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by TYF on 2018/1/29.
 */
public interface SearchService {

    Page<DocEntity> searchLaw(Integer pageNumber, Integer pageSize, String searchAttr, String searchContent);

    Page<DocEntity> multiSearch(Integer pageNumber, Integer pageSize, JSONArray json) throws JSONException;

    Page<DocEntity> similarSearch(Integer pageNumber, Integer pageSize, String searchContent);

    void deleteFavorDoc(int userId, String docId);

    String favorDoc(int userId, String docId);

    List<DocEntity> listFavorDocs(int userId);
}
