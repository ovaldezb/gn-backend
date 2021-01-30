package mx.com.gruponordan.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import mx.com.gruponordan.security.jwt.AuthEntryPointJwt;
import mx.com.gruponordan.security.jwt.AuthTokenFilter;
import mx.com.gruponordan.security.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean

	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.cors().and().csrf().disable().exceptionHandling()
				.authenticationEntryPoint(unauthorizedHandler)
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				/* Seccion para restringir acceso, deshabilitado para pruebas*/
				/*.and()
				.authorizeRequests()
				.antMatchers("/api/auth/**").permitAll()
				.antMatchers("/api/test/**").permitAll()
				.antMatchers("/api/matprima/**").permitAll()
				.antMatchers("/api/bitacora/**").permitAll()
				.antMatchers("/api/file/**").permitAll()
				.antMatchers("/api/ordenfab/**").permitAll()
				.antMatchers("/api/producto/**").permitAll()
				.antMatchers("/api/prodterm/**").permitAll()
				.antMatchers("/api/role/**").permitAll()
				.antMatchers("/api/usuario/**").permitAll()
				.antMatchers("/api/unidad/**").permitAll()
				.antMatchers("/api/prodisp/**").permitAll()
				.antMatchers("/api/matprimdisp/**").permitAll()
				.antMatchers("/api/ordenfab/**").permitAll()
				.antMatchers("/api/utils/**").permitAll()
				.antMatchers("/api/ordencompra/**").permitAll()
				.antMatchers("/api/cliente/**").permitAll()
				.antMatchers("/api/proveedor/**").permitAll()
				.antMatchers("/swagger/**").permitAll()
				.antMatchers("/**").permitAll()	
				.anyRequest()
				.authenticated();*/
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}
