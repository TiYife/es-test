package es.repository.jpaRepository;

import es.entity.jpaEntity.CaseErrorEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by TYF on 2018/5/27.
 */
@Repository
public interface CaseErrorRepository extends CrudRepository<CaseErrorEntity,Integer> {
}
