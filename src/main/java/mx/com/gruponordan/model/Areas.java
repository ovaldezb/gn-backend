package mx.com.gruponordan.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "areas")
public class Areas {

	private String id;
	private String name;
	private EAreas codigo;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public EAreas getCodigo() {
		return codigo;
	}
	public void setCodigo(EAreas codigo) {
		this.codigo = codigo;
	}
	
	
}
