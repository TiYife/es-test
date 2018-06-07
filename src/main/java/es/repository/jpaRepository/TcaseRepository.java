package es.repository.jpaRepository;

import es.entity.jpaEntity.CaseErrorEntity;
import es.entity.jpaEntity.TcaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by TYF on 2018/5/27.
 */
@Repository
public interface TcaseRepository extends CrudRepository<TcaseEntity,Integer> {

    Page<TcaseEntity> findAll(Pageable pageable);
}
