package mx.com.gruponordan.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "materiaprima")
public class MateriaPrima {
	
	@Override
	public String toString() {
		return "MateriaPrima [cantidad=" + cantidad + "]";
	}
	@Id
	private String id;
	private String descripcion;
	private double cantidad;
	private double cantidadOriginal;
	@DBRef
	private UnidadMedida unidad;
	private String codigo;
	@DBRef
	private Proveedor proveedor;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private Date fechaEntrada;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private Date fechaCaducidad;
	private String observaciones;
	private String lote;
	//private long abundante; // >300
	private long necesario; //101-300
	private long escaso; // 0-100
	private boolean activo;
	/*Se pone en true cuando ya se haya ocupado todo el lote de esta MP*/
	private double factor;
	private double apartado;
	private String tipo; //P : Produccion, I: I&D
	private double factorConversion;
	
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
	
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public double getCantidadOriginal() {
		return cantidadOriginal;
	}
	public void setCantidadOriginal(double cantidadOriginal) {
		this.cantidadOriginal = cantidadOriginal;
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
	public Proveedor getProveedor() {
		return proveedor;
	}
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}
	public Date getFechaEntrada() {
		return fechaEntrada;
	}
	public void setFechaEntrada(Date fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}
	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}
	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public long getNecesario() {
		return necesario;
	}
	public void setNecesario(long necesario) {
		this.necesario = necesario;
	}
	public long getEscaso() {
		return escaso;
	}
	public void setEscaso(long escaso) {
		this.escaso = escaso;
	}
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public double getFactor() {
		return factor;
	}
	public void setFactor(double factor) {
		this.factor = factor;
	}
	public double getApartado() {
		return apartado;
	}
	public void setApartado(double apartado) {
		this.apartado = apartado;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public double getFactorConversion() {
		return factorConversion;
	}
	public void setFactorConversion(double factorConversion) {
		this.factorConversion = factorConversion;
	}	
	
}
