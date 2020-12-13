package mx.com.gruponordan.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.User;

public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

}
