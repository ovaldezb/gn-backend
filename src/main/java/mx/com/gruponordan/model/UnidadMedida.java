package mx.com.gruponordan.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "unidad_medida")
public class UnidadMedida {
	
	@Id
	private String id;
	private String unidadMedida;
	
	public UnidadMedida() {
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUnidadMedida() {
		return unidadMedida;
	}
	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}
	

}
