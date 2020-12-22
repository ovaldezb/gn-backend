package mx.com.gruponordan.model;

import java.util.Arrays;

import javax.validation.constraints.Email;
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
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	@DBRef
	private Role[] roles;
	private boolean activo;
	private String nombre;
	private String apellido;
	private String noEmpleado;

	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String id, String username, String email, Role[] roles, String nombre, String apellido, String noEmpleado, boolean activo) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.nombre = nombre;
		this.apellido = apellido;
		this.noEmpleado = noEmpleado;
		this.activo = activo;
	}

	public User(String username, String email, String password, boolean activo, String nombre, String apellido,
			String noEmpleado) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.activo = activo;
		this.nombre = nombre;
		this.apellido = apellido;
		this.noEmpleado = noEmpleado;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role[] getRoles() {
		return roles;
	}

	public void setRoles(Role[] roles) {
		this.roles = roles;
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

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + ", roles="
				+ Arrays.toString(roles) + ", activo=" + activo + ", nombre=" + nombre + ", apellido=" + apellido
				+ ", noEmpleado=" + noEmpleado + "]";
	}
	
	
}
