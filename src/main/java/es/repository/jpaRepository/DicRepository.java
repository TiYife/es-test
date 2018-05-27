package es.repository.jpaRepository;

import es.entity.jpaEntity.DicEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by TYF on 2018/5/27.
 */
public interface DicRepository extends CrudRepository<DicEntity,Integer> {
}
