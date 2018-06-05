package es.repository.jpaRepository;

import es.entity.jpaEntity.TxtEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by 13051 on 2018/2/27.
 */
public interface TxtRepository extends CrudRepository<TxtEntity,String> {

    List<TxtEntity> findAll();

    List<TxtEntity> findByUpLog(String up);
}
