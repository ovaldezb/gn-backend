package mx.com.gruponordan.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.MateriaPrima;


public interface MateriaPrimaDAO extends MongoRepository<MateriaPrima, String> {

}
