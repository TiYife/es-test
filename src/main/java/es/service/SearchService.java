package es.service;

import es.entity.DocEntity;

import java.util.List;

/**
 * Created by TYF on 2018/1/29.
 */
public interface SearchService {

    List<DocEntity> searchLaw(Integer pageNumber, Integer pageSize,String searchAttr, String searchContent);
}
