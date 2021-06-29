package mx.com.gruponordan.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "bases")
public class Bases {
	
	@Id
	private String id;
	private String nombre;
	private MateriaPrimaUsada[] materiaPrimaUsada;
	private String lote;
	private String codigo;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private Date fechaProducccion;
	private Double cantidadOriginal;
	private Double cantidadRestante;
	
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
	public MateriaPrimaUsada[] getMateriaPrimaUsada() {
		return materiaPrimaUsada;
	}
	public void setMateriaPrimaUsada(MateriaPrimaUsada[] materiaPrimaUsada) {
		this.materiaPrimaUsada = materiaPrimaUsada;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Date getFechaProducccion() {
		return fechaProducccion;
	}
	public void setFechaProducccion(Date fechaProducccion) {
		this.fechaProducccion = fechaProducccion;
	}
	public Double getCantidadOriginal() {
		return cantidadOriginal;
	}
	public void setCantidadOriginal(Double cantidadOriginal) {
		this.cantidadOriginal = cantidadOriginal;
	}
	public Double getCantidadRestante() {
		return cantidadRestante;
	}
	public void setCantidadRestante(Double cantidadRestante) {
		this.cantidadRestante = cantidadRestante;
	}
	

}
