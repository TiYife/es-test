package es.service;

import es.entity.esEntity.DocEntity;
import es.entity.jpaEntity.FavoriteEntity;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by TYF on 2018/1/29.
 */
public interface SearchService {

    List<DocEntity> searchLaw(Integer pageNumber, Integer pageSize,String searchAttr, String searchContent);

    List<DocEntity> multiSearch(Integer pageNumber, Integer pageSize, JSONArray json) throws JSONException;

    List<DocEntity> similarSearch(Integer pageNumber, Integer pageSize, String searchContent);

    void favorDoc(int userId, String docId);

    List<FavoriteEntity> listFavorDocs(int userId);
    List<DocEntity> allLaw();
}
