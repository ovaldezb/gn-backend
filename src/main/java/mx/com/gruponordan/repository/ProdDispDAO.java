package mx.com.gruponordan.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.ProductoDisponible;

public interface ProdDispDAO extends MongoRepository<ProductoDisponible, String> {

}
