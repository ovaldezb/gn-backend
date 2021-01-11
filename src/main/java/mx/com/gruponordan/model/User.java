package mx.com.gruponordan.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
	@Id
	private String id;

	@NotBlank
	@Size(max = 20)
	private String username;

	// @NotBlank
	//@Size(max = 50)
	//@Email
	//private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	//@DBRef
	//private Role[] roles;
	@DBRef
	private Areas area;
	private boolean activo;
	private String nombre;
	private String apellido;
	//private String noEmpleado;

	public User(String id, String username, String nombre, String apellido, boolean activo, Areas area) {
		super();
		this.id = id;
		this.username = username;
		//this.email = email;
		//this.roles = roles;
		this.nombre = nombre;
		this.apellido = apellido;
		this.activo = activo;
		this.area = area;
	}

	public User(String username, String password, boolean activo, String nombre, String apellido, Areas area) {
		super();
		this.username = username;
		this.password = password;
		this.activo = activo;
		this.nombre = nombre;
		this.apellido = apellido;
		this.area = area;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public Areas getArea() {
		return area;
	}

	public void setArea(Areas area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", roles="
				+ ", activo=" + activo + ", nombre=" + nombre + ", apellido=" + apellido + "]";
	}
	
	
}
