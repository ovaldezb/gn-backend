package mx.com.gruponordan.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "prod_disp")
public class ProductoDisponible {

	@Id
	private String id;
	private String nombre;
	private String clave;
	private String tipo;
	private MateriaPrimaUsada[] materiaPrimaUsada;
	private double prodxcaja;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public MateriaPrimaUsada[] getMateriaPrimaUsada() {
		return materiaPrimaUsada;
	}
	public void setMateriaPrimaUsada(MateriaPrimaUsada[] materiaPrimaUsada) {
		this.materiaPrimaUsada = materiaPrimaUsada;
	}
	public double getProdxcaja() {
		return prodxcaja;
	}
	public void setProdxcaja(double prodxcaja) {
		this.prodxcaja = prodxcaja;
	}
}
