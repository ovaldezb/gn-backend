package mx.com.gruponordan.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.Role;

public interface RolesDAO extends MongoRepository<Role, String> {

}
