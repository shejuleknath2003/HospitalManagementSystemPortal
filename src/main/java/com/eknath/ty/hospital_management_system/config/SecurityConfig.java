package com.eknath.ty.hospital_management_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// 1. Disable CSRF for REST API POST/PUT/DELETE calls
				.csrf(csrf -> csrf.disable())

				// 2. Configure endpoint authorization
				.authorizeHttpRequests(auth -> auth
						// Allow static resources (HTML, CSS, JS) and Swagger docs without login
						.requestMatchers("/", "/index.html", "/style.css", "/app.js", "/swagger-ui/**", "/v3/api-docs/**")
						.permitAll()
						// All other API endpoints require authentication
						.anyRequest().authenticated())

				// 3. Enable HTTP Basic Authentication
				.httpBasic(Customizer.withDefaults());

		return http.build();
	}

	// Configure In-Memory Users
	@Bean
	public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
		UserDetails admin = User.withUsername("admin")
				.password(passwordEncoder.encode("1234"))
				.roles("ADMIN")
				.build();

		return new InMemoryUserDetailsManager(admin);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
