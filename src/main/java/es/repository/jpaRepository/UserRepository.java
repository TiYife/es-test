package es.repository.jpaRepository;

import es.entity.jpaEntity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity,Integer> {
    UserEntity findById(int id);

    UserEntity findByUserName(String name);

    List<UserEntity> findAll();

    List<UserEntity> findByRole(int i);
}
