package mx.com.gruponordan.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sequence")
public class Sequence {

	//@Transient
    //public static final String SEQUENCE_NAME = "ordenfab_sequence";

    @Id
    private String id;
    
    private long counter;

	public long getCounter() {
		return counter;
	}

	public void setCounter(long counter) {
		this.counter = counter;
	}

	@Override
	public String toString() {
		return "Sequence [counter=" + counter + "]";
	}

	
}
