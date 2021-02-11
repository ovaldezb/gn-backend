package mx.com.gruponordan.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import mx.com.gruponordan.model.Sequence;

public interface SequenceDAO extends MongoRepository<Sequence, String > {

}
