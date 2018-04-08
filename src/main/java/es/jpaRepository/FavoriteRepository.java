package es.jpaRepository;

import es.entity.FavoriteEntity;
import org.springframework.data.repository.CrudRepository;

public interface FavoriteRepository extends CrudRepository<FavoriteEntity,String> {
}
