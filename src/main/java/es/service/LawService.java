package es.service;

import es.entity.LawEntity;

import java.util.List;

/**
 * Created by TYF on 2018/1/29.
 */
public interface LawService {

    String saveLaw(LawEntity law);

    List<LawEntity> searchLaw(Integer pageNumber, Integer pageSize, String searchContent);
}
