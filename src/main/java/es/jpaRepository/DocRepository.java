package es.jpaRepository;

import es.entity.DocumentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by 13051 on 2018/2/27.
 */
public interface DocRepository extends CrudRepository<DocumentEntity,String> {
    List<DocumentEntity> findAllByUploadedAndAndDeleted(boolean up,boolean del);
}
