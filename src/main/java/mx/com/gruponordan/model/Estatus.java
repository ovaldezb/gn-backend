package mx.com.gruponordan.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "estatus")
public class Estatus {

	@Id
	private String id;
	private Eestatus codigo;
	private String label;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Eestatus getCodigo() {
		return codigo;
	}
	public void setCodigo(Eestatus codigo) {
		this.codigo = codigo;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	@Override
	public String toString() {
		return "Estatus [id=" + id + ", codigo=" + codigo + ", label=" + label + "]";
	}
	
	
}
