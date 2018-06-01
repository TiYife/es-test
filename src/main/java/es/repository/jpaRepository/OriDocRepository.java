package es.repository.jpaRepository;

import es.entity.jpaEntity.OriDocEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by 13051 on 2018/2/27.
 */
public interface OriDocRepository extends CrudRepository<OriDocEntity,String> {

    List<OriDocEntity> findAll();

    List<OriDocEntity> findBySave(boolean b);
}
