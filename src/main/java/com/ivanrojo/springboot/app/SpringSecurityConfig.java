package com.ivanrojo.springboot.app;

//import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

import com.ivanrojo.springboot.app.auth.filter.JWTAuthenticationFilter;
import com.ivanrojo.springboot.app.auth.filter.JWTAuthorizationFilter;
import com.ivanrojo.springboot.app.auth.handler.LoginSuccessHandler;
import com.ivanrojo.springboot.app.auth.service.JWTService;
import com.ivanrojo.springboot.app.models.service.JpaUserDetailsService;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig  extends WebSecurityConfigurerAdapter{
	
	@Autowired 
	private LoginSuccessHandler successHandler;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JpaUserDetailsService userDetailsService;
	@Autowired
	private JWTService jwtService;
	
//	@Autowired
//	private DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
			.antMatchers("/","/css/**","/js/**","/listar"/*, "/api/**"*/, "/locale").permitAll()
//			.antMatchers("/cliente/**").hasAnyRole("USER")
//			.antMatchers("/uploads/**").hasAnyRole("USER")
//			.antMatchers("/factura/show/**").hasAnyRole("USER")
//			.antMatchers("/create").hasAnyRole("ADMIN")
//			.antMatchers("/edit/**").hasAnyRole("ADMIN")
//			.antMatchers("/create").hasAnyRole("ADMIN")
//			.antMatchers("/update/**").hasAnyRole("ADMIN")
//			.antMatchers("/delete/**").hasAnyRole("ADMIN")
//			.antMatchers("/factura/**/create").hasAnyRole("ADMIN")
//			.antMatchers("/factura/store/").hasAnyRole("ADMIN")
//			.antMatchers("/factura/delete/**").hasAnyRole("ADMIN")
			.antMatchers("/h2-console/**").permitAll()
			.anyRequest().authenticated()
			/*.and()
			.formLogin().successHandler(successHandler).loginPage("/login").permitAll()
			.and()
			.exceptionHandling()
			.accessDeniedPage("/error_403")
			.and()
			.logout()
			.permitAll()*/
			.and()
            .headers().frameOptions().disable()
            .and()
			.addFilter(new JWTAuthorizationFilter(authenticationManager(),jwtService))
			.addFilter(new JWTAuthenticationFilter(authenticationManager(),jwtService))
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//            .and()
//            .csrf().ignoringAntMatchers("/h2-console/**")
//            .and()
//            .cors().disable();
		
	}

	
	
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception{
//		143 Con JPA User Details Service
		builder.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder);
//		PasswordEncoder encoder = this.passwordEncoder;
//		Usar el password encoder con funciones de flecha
//		UserBuilder users = User.builder().passwordEncoder(password->encoder.encode(password));
//		Usar el password encoder como funcion lambda y guardar usuarios en memoria
//		UserBuilder users = User.builder().passwordEncoder(encoder::encode);
//		builder.inMemoryAuthentication()
//						.withUser(users.username("admin")
//						.password("Prueba12345")
//						.roles("ADMIN","USER"))
//						.withUser(users.username("ivan")
//						.password("Prueba12345")
//						.roles("USER"));
//		Crear usuarios en base de datos
//		builder.jdbcAuthentication()
//				.dataSource(dataSource)
//				.passwordEncoder(passwordEncoder)
//				.usersByUsernameQuery("select username, password, enabled from users where username =?")
//				.authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id = u.id) where u.username=?");
	
		
	}
}
