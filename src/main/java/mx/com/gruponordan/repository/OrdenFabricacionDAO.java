package mx.com.gruponordan.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.OrdenFabricacion;

public interface OrdenFabricacionDAO extends MongoRepository<OrdenFabricacion, String> {

}
