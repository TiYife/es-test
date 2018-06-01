package es.repository.jpaRepository;

import es.entity.jpaEntity.UpLogEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by TYF on 2018/5/27.
 */
public interface UpLogRepository extends CrudRepository<UpLogEntity,String > {
    List<UpLogEntity> findByIsSaveOrderByUpTime(byte i);
}
