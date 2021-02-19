package mx.com.gruponordan.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.ProductoEntregado;

public interface ProductoEntregadoDAO extends MongoRepository<ProductoEntregado, String> {

}
