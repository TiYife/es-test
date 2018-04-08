package es.repository.jpaRepository;

import es.entity.jpaEntity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity,Integer> {
    public UserEntity findByUserId(int id);
}
