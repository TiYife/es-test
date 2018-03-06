package es.jpaRepository;

import es.entity.DocumentsEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by 13051 on 2018/2/27.
 */
public interface DocRepository extends CrudRepository<DocumentsEntity,String> {
    public List<DocumentsEntity> findAllByUploadedAndAndDeleted(boolean up,boolean del);
}
