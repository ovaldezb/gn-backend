package mx.com.gruponordan.model;

/**
 * Este Bean sera ocupado para almacenar las materias primas usadas en una OF*/
public class MatPrimaOrdFab {

	private String codigo;
	private String nombre;
	private double cantidad;
	private String lote;
	private String estatus;
	private String comentarios;
	
	public MatPrimaOrdFab(String codigo,String nombre, double cantidad, String lote, String estatus, String comentarios) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.lote = lote;
		this.estatus = estatus;
		this.comentarios = comentarios;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	@Override
	public String toString() {
		return "MatPrimaOrdFab [codigo=" + codigo + ", nombre=" + nombre + ", cantidad=" + cantidad + ", lote=" + lote
				+ ", estatus=" + estatus + ", comentarios=" + comentarios + "]";
	}
	
}