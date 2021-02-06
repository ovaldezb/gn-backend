package mx.com.gruponordan.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import mx.com.gruponordan.security.service.UserDetailsServiceImpl;

public class AuthTokenFilter extends OncePerRequestFilter {
	
	
	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			//System.out.println(request.getLocalName()+" "+request.getLocalPort());
			String jwt = parseJwt(request);
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				String username = jwtUtils.getUserNameFromJwtToken(jwt);
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}else if(!request.getRequestURI().toString().equals("/api/auth/signin")){	
				response.sendError(HttpStatus.UNAUTHORIZED.value());
			}
			/*else if(!request.getRequestURI().toString().equals("/api/auth/signin") && !request.getRequestURI().toString().equals("/v2/api-docs") 
					&& !request.getRequestURI().toString().contains("swagger") && !request.getRequestURI().toString().contains("/webjars") ){
				
				response.sendError(HttpStatus.UNAUTHORIZED.value());
			}*/
			
		} catch (ExpiredJwtException e) {
			logger.error("Error", e);
			response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
		} catch(Exception e) {
			logger.error("Error", e);
		}

		filterChain.doFilter(request, response);
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}

		return null;
	}

}
