package mx.com.gruponordan.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.Areas;

public interface AreasDAO extends MongoRepository<Areas, String> {

}
