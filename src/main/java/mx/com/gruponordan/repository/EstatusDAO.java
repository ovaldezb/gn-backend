package mx.com.gruponordan.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.Eestatus;
import mx.com.gruponordan.model.Estatus;

public interface EstatusDAO extends MongoRepository<Estatus, String> {
	Estatus findByCodigo(Eestatus codigo);
}
