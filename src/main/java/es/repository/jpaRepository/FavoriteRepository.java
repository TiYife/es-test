package es.repository.jpaRepository;

import es.entity.jpaEntity.FavoriteEntity;
import org.springframework.data.repository.CrudRepository;

public interface FavoriteRepository extends CrudRepository<FavoriteEntity,String> {
}
