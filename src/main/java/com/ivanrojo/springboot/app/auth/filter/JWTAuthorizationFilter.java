package com.ivanrojo.springboot.app.auth.filter;

import java.io.IOException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.ivanrojo.springboot.app.auth.service.JWTService;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    
	private static final Logger log = LoggerFactory.getLogger(JWTAuthorizationFilter.class);


	private JWTService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader("Authorization");
		log.info(header);
		if (!requiresAuthentication(header)) {
			chain.doFilter(request, response);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = null;
		if (jwtService.validate(header)) {
			log.info("entra al validate");

			authentication = new UsernamePasswordAuthenticationToken(jwtService.getUsername(header), null,
					jwtService.getRoles(header));

		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);

//		super.doFilterInternal(request, response, chain);
	}

	protected boolean requiresAuthentication(String header) {
//		Validamos que el usuario el header exista y no empieze con Bearer 
		if (header == null || !header.startsWith("Bearer ")) {
			return false;
		} else {
			return true;
		}
	}

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
		super(authenticationManager);
		this.jwtService = jwtService;
		// TODO Auto-generated constructor stub
	}

}
