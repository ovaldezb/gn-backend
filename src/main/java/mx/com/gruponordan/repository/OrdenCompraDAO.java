package mx.com.gruponordan.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.OrdenCompra;

public interface OrdenCompraDAO extends MongoRepository<OrdenCompra, String> {

}
