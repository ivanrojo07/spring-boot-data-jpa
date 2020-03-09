package com.ivanrojo.springboot.app.auth.service;

import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.jackson2.SimpleGrantedAuthorityMixin;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTServiceImpl implements JWTService{
	
//	public static final String SECRET_KEY = "hola mundo";
	public static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//	public static final SecretKey SECRET_KEY =  Keys.hmacShaKeyFor("algunaLlaveSecreta".getBytes());

//    public static final String JWT_SECRET = "n2r5u8x/A%D*G-KaPdSgVkYp3s6v9y$B&E(H+MbQeThWmZq4t7w!z%C*F-J@NcRf";

	
	public static final long EXPIRATION_DATE = 14000000L;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	
	private static final Logger log = LoggerFactory.getLogger(JWTServiceImpl.class);
	
	
	@Override
	public String create(Authentication auth) throws IOException {
		String username = ((User) auth.getPrincipal()).getUsername();
		
		Collection<? extends GrantedAuthority> roles = auth.getAuthorities();

		Claims claims = Jwts.claims();
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
		
		SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
		 //or HS384 or HS512
//		String base64Key = Encoders.BASE64.encode(key.getEncoded());
//		var signingKey = JWT_SECRET.getBytes();
//		log.info("SECRET_KEY "+ SECRET_KEY.toString());

//		String token = Jwts.builder()
//				.setClaims(claims)
//				.signWith(SECRET_KEY)
//				.setHeaderParam("typ", "JWT")
//				.setIssuer("secure-api")
//				.setAudience("secure-app")
//				.setSubject(username)
//				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
////				.claim("roles",roles)
//				.compact();
				
		String token = Jwts.builder()
						.setClaims(claims)
						.setSubject(username)
						.signWith(SECRET_KEY)
						.setIssuedAt(new Date())
						.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
						.compact();
		return token;
	}

	@Override
	public boolean validate(String token) {
		try {
			getClaims(token);
			return true;
		}catch (JwtException | IllegalArgumentException e) {
			log.info(e.toString());
			return false;
		}
	}

	@Override
	public Claims getClaims(String token) throws JwtException, IllegalArgumentException{
		Claims claims = Jwts.parserBuilder()
	                .setSigningKey(SECRET_KEY)
	                .build()
	                .parseClaimsJws(resolve(token)).getBody();
		return claims;
	}

	@Override
	public String getUsername(String token) {
//		log.info("GET USERNAME "+getClaims(token).getSubject());
		return getClaims(token).getSubject();
	}

	@Override
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
		String roles = getClaims(token).get("authorities",String.class);
		Collection<GrantedAuthority> authorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
//		log.info("Get Roles "+roles.toString());
//		Collection<? extends GrantedAuthority> authorities = Arrays.asList( new ObjectMapper()
//				.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
//				.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
//		log.info("authorities "+authorities.toString());
		return authorities;
	}

	@Override
	public String resolve(String token) {
		if (token != null && token.startsWith(TOKEN_PREFIX)) {
			return token.replace(TOKEN_PREFIX,"");
		}
		return null;
	}

}
