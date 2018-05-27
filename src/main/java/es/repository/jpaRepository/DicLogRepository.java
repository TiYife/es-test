package es.repository.jpaRepository;

import es.entity.jpaEntity.DicLogEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by TYF on 2018/5/27.
 */
public interface DicLogRepository extends CrudRepository<DicLogEntity,Integer> {
}
