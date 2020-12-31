package mx.com.gruponordan.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.BitacoraActv;
import mx.com.gruponordan.model.EBitacora;

public interface BitacoraActvDAO extends MongoRepository<BitacoraActv, String> {
	Optional<BitacoraActv> findByName(EBitacora name);
}
