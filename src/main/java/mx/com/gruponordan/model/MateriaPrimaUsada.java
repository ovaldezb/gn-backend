package mx.com.gruponordan.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
 * Este documento se va a utiizar para almacenar la materia prima usada en un Producto Disponible
 * Va hacer referencia a una Materia Prima, y una cantidad, la cual sera descontada de la MP cuando se genere una OF
 * */

@Document(collection = "matprim_usada")
public class MateriaPrimaUsada {
	
	@Id
	private String id;
	private MateriaPrimaDisponible materiaprimadisponible;
	private double cantidad;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public MateriaPrimaDisponible getMateriaprimadisponible() {
		return materiaprimadisponible;
	}
	public void setMateriaprimadisponible(MateriaPrimaDisponible materiaprimadisponible) {
		this.materiaprimadisponible = materiaprimadisponible;
	}
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	

}
