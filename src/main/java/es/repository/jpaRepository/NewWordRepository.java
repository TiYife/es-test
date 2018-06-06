package es.repository.jpaRepository;

import es.entity.jpaEntity.NewWordEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by TYF on 2018/5/27.
 */
@Repository
public interface NewWordRepository extends CrudRepository<NewWordEntity,Integer> {
}
