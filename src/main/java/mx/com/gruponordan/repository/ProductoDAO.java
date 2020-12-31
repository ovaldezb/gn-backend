package mx.com.gruponordan.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.ProductoDisponible;

public interface ProductoDAO extends MongoRepository<ProductoDisponible, String> {

}
