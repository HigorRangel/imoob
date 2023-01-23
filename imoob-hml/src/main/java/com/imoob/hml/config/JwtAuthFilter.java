package com.imoob.hml.config;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.imoob.hml.model.exceptions.StandardError;
import com.imoob.hml.service.AuthenticationService;
import com.imoob.hml.service.PermissionService;
import com.imoob.hml.service.utils.GeneralUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	private final UserDetailsService userDetailsService;
	
	private final GeneralUtils generalUtils;
	
	private final PermissionService permissionService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		final String userEmail;
		final String jwtToken;
		
		System.out.println(request.getRequestURI());

		try {
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}
			jwtToken = authHeader.substring(7);
			userEmail = jwtService.extractUsername(jwtToken);
			if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

				if (jwtService.isTokenValid(jwtToken, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());

					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
				
				if(!permissionService.validatePermission(request.getRequestURI(), userDetails)) {
					
				}
			}
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			String error = "JWT Expired.";

			StandardError err = new StandardError(Instant.now(), HttpStatus.BAD_REQUEST.value(), error, e.getMessage(),
					request.getRequestURI());

			response.setContentType("application/json");
			response.getWriter().write(generalUtils.convertObjectToJson(err));
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	

}
