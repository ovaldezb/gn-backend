package mx.com.gruponordan.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cliente")
public class Cliente {
	@Id
	private String id;
	private String nombre;
	private String[] direccion;
	private String rfc;
	private Contacto[] contactos;
	private boolean activo;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public Contacto[] getContactos() {
		return contactos;
	}
	public void setContactos(Contacto[] contactos) {
		this.contactos = contactos;
	}
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public String[] getDireccion() {
		return direccion;
	}
	public void setDireccion(String[] direccion) {
		this.direccion = direccion;
	}
	
}
