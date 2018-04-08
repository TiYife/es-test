package es.jpaRepository;

import es.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity,Integer> {
    public UserEntity findByUserId(int id);
}
