package mx.com.gruponordan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.User;

public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByUsername(String username);
  
  List<User> findByActivo(boolean activo);

  Boolean existsByUsername(String username);

}
