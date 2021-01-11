package mx.com.gruponordan.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignupRequest {
	@NotBlank
	@Size(min = 3,max = 20)
	private String username;
	//private Role[] roles;	
	@NotBlank
	@Size(min = 6,max = 40)
	private String password;
	private boolean activo;
	private String nombre;
	private String apellido;
	private String noEmpleado;
	private Areas area;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getNoEmpleado() {
		return noEmpleado;
	}
	public void setNoEmpleado(String noEmpleado) {
		this.noEmpleado = noEmpleado;
	}
	public Areas getArea() {
		return area;
	}
	public void setArea(Areas area) {
		this.area = area;
	}
	
}
