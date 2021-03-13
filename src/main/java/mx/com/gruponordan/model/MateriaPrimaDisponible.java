package mx.com.gruponordan.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Este documento sera usado para almacenar unicamente las materias primas con las que cuenta la empresa
 * seran usados como referencia al momento de dar de alta un Producto Disponible
 * */
@Document(collection = "matprim_disp")
public class MateriaPrimaDisponible {

	@Id
	private String id;
	private String descripcion;
	@DBRef
	private UnidadMedida unidad ;
	private String codigo;
	//private long necesario; //101-300
	//private long escaso; // 0-100
	
	public MateriaPrimaDisponible(String descripcion, UnidadMedida unidad, String codigo) {
		super();
		this.descripcion = descripcion;
		this.unidad = unidad;
		this.codigo = codigo;
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public UnidadMedida getUnidad() {
		return unidad;
	}
	public void setUnidad(UnidadMedida unidad) {
		this.unidad = unidad;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	
}
