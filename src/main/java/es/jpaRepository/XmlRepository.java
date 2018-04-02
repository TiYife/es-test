package es.jpaRepository;

import es.entity.XmlEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by 13051 on 2018/2/27.
 */
public interface XmlRepository extends CrudRepository<XmlEntity,String> {
    List<XmlEntity> findByUpAndDel(boolean up, boolean del);
}
