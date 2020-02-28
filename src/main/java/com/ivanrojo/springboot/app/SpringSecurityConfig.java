package com.ivanrojo.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringSecurityConfig  extends WebSecurityConfigurerAdapter{
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
			.antMatchers("/","/css/**","/js/**","/listar").permitAll()
			.antMatchers("/cliente/**").hasAnyRole("USER")
			.antMatchers("/uploads/**").hasAnyRole("USER")
			.antMatchers("/factura/show/**").hasAnyRole("USER")
			.antMatchers("/create").hasAnyRole("ADMIN")
			.antMatchers("/edit/**").hasAnyRole("ADMIN")
			.antMatchers("/create").hasAnyRole("ADMIN")
			.antMatchers("/update/**").hasAnyRole("ADMIN")
			.antMatchers("/factura/**/create").hasAnyRole("ADMIN")
			.antMatchers("/factura/store/").hasAnyRole("ADMIN")
			.antMatchers("/factura/delete/**").hasAnyRole("ADMIN")
			.antMatchers("/h2-console/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin().loginPage("/login").permitAll()
			.and()
			.logout()
			.permitAll()
			.and()
            .headers().frameOptions().disable()
            .and()
            .csrf().ignoringAntMatchers("/h2-console/**")
            .and()
            .cors().disable();
		
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception{
		PasswordEncoder encoder = passwordEncoder();
//		UserBuilder users = User.builder().passwordEncoder(password->encoder.encode(password));
		UserBuilder users = User.builder().passwordEncoder(encoder::encode);
		builder.inMemoryAuthentication()
						.withUser(users.username("admin")
						.password("Prueba12345")
						.roles("ADMIN","USER"))
						.withUser(users.username("ivan")
						.password("Prueba12345")
						.roles("USER"));
	}
}
