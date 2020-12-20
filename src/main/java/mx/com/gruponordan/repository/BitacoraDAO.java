package mx.com.gruponordan.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.Bitacora;

public interface BitacoraDAO extends MongoRepository<Bitacora, String> {
	//@Query("{'fechaEvento':{$gte:?0,$lte:}}")
	public List<Bitacora> findByFechaEventoBetween(Date fechaIni,Date fechaFin);
	public List<Bitacora> findByUserId(String userId);
	
}
