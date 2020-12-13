package mx.com.gruponordan.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orden_fabriacion")
public class OrdenFabricacion {
	
	@Id
	private String id;
	private String nombreProducto;
	private String clave;
	private Date fechaFabricacion;
	private Date fechaEntrega;
	private String idConsecutivo;
	private String numLote;
	private int piezasAFabricar;
	private String observaciones;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombreProducto() {
		return nombreProducto;
	}
	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public Date getFechaFabricacion() {
		return fechaFabricacion;
	}
	public void setFechaFabricacion(Date fechaFabricacion) {
		this.fechaFabricacion = fechaFabricacion;
	}
	public Date getFechaEntrega() {
		return fechaEntrega;
	}
	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	public String getIdConsecutivo() {
		return idConsecutivo;
	}
	public void setIdConsecutivo(String idConsecutivo) {
		this.idConsecutivo = idConsecutivo;
	}
	public String getNumLote() {
		return numLote;
	}
	public void setNumLote(String numLote) {
		this.numLote = numLote;
	}
	public int getPiezasAFabricar() {
		return piezasAFabricar;
	}
	public void setPiezasAFabricar(int piezasAFabricar) {
		this.piezasAFabricar = piezasAFabricar;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	

}
