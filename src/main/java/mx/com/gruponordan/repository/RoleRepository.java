package mx.com.gruponordan.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.ERole;
import mx.com.gruponordan.model.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
