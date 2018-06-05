package es.repository.jpaRepository;

import es.entity.jpaEntity.UpLogEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by TYF on 2018/5/27.
 */
public interface UpLogRepository extends CrudRepository<UpLogEntity,String > {
    List<UpLogEntity> findByIsSave(int i);

    @Query("select t from UpLogEntity t order by t.upTime desc")
    List<UpLogEntity> findAllOrderByUpTime();

    @Query("select t from UpLogEntity t where t.isSave <> ?1 order by t.upTime  ")
    List<UpLogEntity> findByIsSaveNotEqualsOrderByUpTime(int i);
}
