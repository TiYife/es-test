package es.repository.jpaRepository;

import es.entity.jpaEntity.FavoriteEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FavoriteRepository extends CrudRepository<FavoriteEntity,Integer> {

    List<FavoriteEntity> findByUserId(int userId);
}
