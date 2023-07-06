package AndreaBarocchi.CapstoneProject.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.*;
import AndreaBarocchi.CapstoneProject.exceptions.ExceptionHandlerFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	@Autowired
	JWTAuthFilter jwtAuthFilter;
	@Autowired
	CorsFilter corsFilter;
	@Autowired
	ExceptionHandlerFilter exceptionHandlerFilter;
	
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(c -> c.disable());
		
		//work in progress, trying to implement login with Google
		http
        .authorizeHttpRequests(auth -> {
            auth.requestMatchers("/").permitAll();
            auth.requestMatchers("/secured").authenticated();
        })
        .oauth2Login(withDefaults())
        .formLogin(withDefaults());

		
		
		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers("/auth/**", "/login/**")
			.permitAll();
			});
		
		
		//auth user
		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers(HttpMethod.GET, "/users").hasAnyAuthority("USER", "ADMIN");
			auth.requestMatchers(HttpMethod.GET, "/users/me").authenticated();
			auth.requestMatchers(HttpMethod.PUT, "/users/**").hasAnyAuthority("USER", "ADMIN");
			auth.requestMatchers(HttpMethod.DELETE, "/users/**").hasAnyAuthority("USER", "ADMIN");
			auth.requestMatchers("/users/**").hasAnyAuthority("USER", "ADMIN");
		});
		
		//auth articles
		http.authorizeHttpRequests(auth -> {
		    auth.requestMatchers(HttpMethod.GET, "/articles").hasAnyAuthority("USER", "ADMIN");
		    auth.requestMatchers(HttpMethod.GET, "/articles/**").hasAnyAuthority("USER", "ADMIN");
		    auth.requestMatchers(HttpMethod.PUT, "/articles/**").hasAnyAuthority("USER", "ADMIN");
		    auth.requestMatchers(HttpMethod.DELETE, "/articles/**").hasAnyAuthority("USER", "ADMIN");
		    auth.requestMatchers("/articles/**").hasAnyAuthority("USER", "ADMIN");
		});


		//auth comment
		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers(HttpMethod.GET, "/comments").hasAnyAuthority("USER", "ADMIN");
			auth.requestMatchers(HttpMethod.PUT, "/comments/**").hasAnyAuthority("USER", "ADMIN");
			auth.requestMatchers(HttpMethod.DELETE, "/comments/**").hasAnyAuthority("USER", "ADMIN");
			auth.requestMatchers("/comments/**").hasAnyAuthority("USER", "ADMIN");
		});
		
		//auth category
		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers(HttpMethod.GET, "/categories").hasAnyAuthority("USER", "ADMIN");
			auth.requestMatchers(HttpMethod.GET, "/categories/name/**").hasAnyAuthority("USER", "ADMIN");
			auth.requestMatchers(HttpMethod.GET, "/categories/**").hasAnyAuthority("USER", "ADMIN");
			auth.requestMatchers("/categories/**").hasAuthority("ADMIN");
		});
		
		//auth likes
		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers(HttpMethod.POST, "/likes/**").hasAnyAuthority("USER", "ADMIN");
			auth.requestMatchers(HttpMethod.DELETE, "/likes/**").hasAnyAuthority("USER", "ADMIN");
		});
		
		http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);

		http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		http.addFilterBefore(exceptionHandlerFilter, JWTAuthFilter.class);

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}


	@Bean
	PasswordEncoder pwEncoder() {
		return new BCryptPasswordEncoder(10);
	}


}