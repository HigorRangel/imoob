package com.imoob.hml.config;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.imoob.hml.model.exceptions.StandardError;
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

				if (!permissionService.validatePermission(request.getRequestURI(), userDetails)) {

				}
			}
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			errorExpiredJwt(request, response, e);

		} catch (UsernameNotFoundException e) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			String error = "JWT inválido. Usuário não encontrado.";

			StandardError err = new StandardError(Instant.now(), HttpStatus.FORBIDDEN.value(), error, e.getMessage(),
					e.getCause(), request.getRequestURI());

			response.setContentType("application/json");
			response.getWriter().write(GeneralUtils.convertObjectToJson(err));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
//
//	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
//			@NonNull FilterChain filterChain) throws ServletException, IOException {
//
//		final String authHeader = request.getHeader("Authorization");
//
//		if (!isValidAuthorizationHeader(authHeader)) {
//			filterChain.doFilter(request, response);
//			return;
//		}
//
//		UserDetails userDetails = null;
//		String jwtToken = authHeader.substring(7);
//
//		try {
//			userDetails = loadUserDetails(authHeader);
//		} catch (ExpiredJwtException e) {
//			errorExpiredJwt(request, response, e);
//		} catch (UsernameNotFoundException e) {
//			response.setStatus(HttpStatus.FORBIDDEN.value());
//			String error = "JWT inválido. Usuário não encontrado.";
//
//			StandardError err = new StandardError(Instant.now(), HttpStatus.FORBIDDEN.value(), error, e.getMessage(),
//					e.getCause(), request.getRequestURI());
//
//			response.setContentType("application/json");
//			response.getWriter().write(GeneralUtils.convertObjectToJson(err));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		if (!isTokenValid(jwtToken, userDetails)) {
//			filterChain.doFilter(request, response);
//			return;
//		}
//
//		if (!permissionService.validatePermission(request.getRequestURI(), userDetails)) {
//			// tratar a falta de permissão aqui
//			return;
//		}
//
//		setAuthToken(request, userDetails);
//		filterChain.doFilter(request, response);
//	}
//
//	private boolean isValidAuthorizationHeader(String authHeader) {
//		return authHeader != null && authHeader.startsWith("Bearer ");
//	}
//
//	private UserDetails loadUserDetails(String authHeader) {
//		return jwtService.loadUserDetails(authHeader);
//	}
//
//	private boolean isTokenValid(String jwtToken, UserDetails userDetails) {
//		return userDetails != null && jwtService.isTokenValid(jwtToken, userDetails);
//	}
//
//	private void setAuthToken(HttpServletRequest request, UserDetails userDetails) {
//		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
//				userDetails.getAuthorities());
//		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//		SecurityContextHolder.getContext().setAuthentication(authToken);
//	}
//
	private void errorExpiredJwt(HttpServletRequest request, HttpServletResponse response, ExpiredJwtException e)
			throws IOException, JsonProcessingException {
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		String error = "JWT Expired.";

		StandardError err = new StandardError(Instant.now(), HttpStatus.BAD_REQUEST.value(), error, e.getMessage(),
				request.getRequestURI());

		response.setContentType("application/json");
		response.getWriter().write(GeneralUtils.convertObjectToJson(err));
	}

}
