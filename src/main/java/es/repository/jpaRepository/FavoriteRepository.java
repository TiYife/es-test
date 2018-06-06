package es.repository.jpaRepository;

import es.entity.jpaEntity.FavoriteEntity;
import org.springframework.data.repository.CrudRepository;

import javax.print.DocFlavor;
import java.util.List;

public interface FavoriteRepository extends CrudRepository<FavoriteEntity,Integer> {
    List<FavoriteEntity> findByUserId(int userId);

    List<FavoriteEntity> findByUserIdAndDocId(int userId, String  docId);
}
