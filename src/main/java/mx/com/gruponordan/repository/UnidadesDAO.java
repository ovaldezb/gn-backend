package mx.com.gruponordan.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.UnidadMedida;

public interface UnidadesDAO extends MongoRepository<UnidadMedida, String> {
	UnidadMedida findByUnidadMedida(String unidad);
}
