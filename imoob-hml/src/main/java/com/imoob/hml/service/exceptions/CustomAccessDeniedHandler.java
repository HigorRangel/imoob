package com.imoob.hml.service.exceptions;

import java.io.IOException;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.imoob.hml.model.exceptions.StandardError;
import com.imoob.hml.service.utils.GeneralUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	
	@Autowired
	private GeneralUtils generalUtils;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			String error = "User not allowed to perform this task.";

			StandardError err = new StandardError(Instant.now(), HttpStatus.FORBIDDEN.value(), error, "Access Denied",
					request.getRequestURI());

			response.setContentType("application/json");

			response.getWriter().write(generalUtils.convertObjectToJson(err));
		}
//		        response.sendRedirect(request.getContextPath() + "/accessDenied");

	}

}
