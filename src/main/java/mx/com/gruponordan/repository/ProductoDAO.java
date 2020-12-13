package mx.com.gruponordan.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.Producto;

public interface ProductoDAO extends MongoRepository<Producto, String> {

}
