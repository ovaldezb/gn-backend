package mx.com.gruponordan.security.service;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import mx.com.gruponordan.model.User;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String id;

	private String username;

	@JsonIgnore
	private String password;
	
	private boolean activo;
	
	private String area;
	
	private String codeArea;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(String id, String username, String password,boolean activo, String area, String codeArea) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.activo = activo;
		this.area = area;
		this.codeArea = codeArea;
		//this.authorities = authorities;
	}

	public static UserDetailsImpl build(User user) {
		/*
		 * List<GrantedAuthority> authorities = Arrays.asList( user.getRoles()).stream()
		 * .map(role -> new
		 * SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
		 */

		return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(), user.isActivo(), user.getArea().getName(), user.getArea().getCodigo().name());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getId() {
		return id;
	}
	
	public boolean isActivo() {
		return activo;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public String getArea() {
		return area;
	}
	
	public String getCodeArea() {
		return codeArea;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public String toString() {
		return "UserDetailsImpl [id=" + id + ", username=" + username + ", password=" + password + ", activo=" + activo
				+ ", area=" + area + ", codeArea=" + codeArea + "]";
	}

}
