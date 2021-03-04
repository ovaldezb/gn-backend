package mx.com.gruponordan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.Eestatus;
import mx.com.gruponordan.model.Lote;

public interface LoteDAO extends MongoRepository<Lote, String> {
	List<Lote> findByEstatusNotLike(Eestatus estatus);
	List<Lote> findByLoteLikeAndAprobado(String lote, boolean aprobado);
	Optional<Lote> findByLote(String lote);
}
