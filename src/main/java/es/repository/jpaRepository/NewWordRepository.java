package es.repository.jpaRepository;

import es.entity.jpaEntity.NewWordEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by TYF on 2018/5/27.
 */
public interface NewWordRepository extends CrudRepository<NewWordEntity,Integer> {
}
