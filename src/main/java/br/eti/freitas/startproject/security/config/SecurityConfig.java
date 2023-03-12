package br.eti.freitas.startproject.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.eti.freitas.startproject.security.filter.SecurityRequestFilter;
import br.eti.freitas.startproject.security.handler.AuthenticationEntryPointJwt;
import br.eti.freitas.startproject.security.service.impl.SecurityUserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

	@Autowired
	private AuthenticationEntryPointJwt authenticationEntryPointJwt;

	@Autowired
	private SecurityUserDetailsServiceImpl customUserDetailsService;

	@Autowired
	private SecurityRequestFilter jwtRequestFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(customUserDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	private static final String[] AUTH_WHITELIST = {
	        "/swagger-resources/**",
	        "/swagger-ui.html",
	        "/swagger-ui/**",
	        "/v2/api-docs/**",
	        "/webjars/**",
	        "/error/**",
	        "/favicon.ico"
	};

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.cors()
		        .and()
		        .csrf()
		        .disable()
		        .headers()
		        .frameOptions()
		        .deny()
		        .and()
		        .authorizeRequests().antMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
		                            .antMatchers(AUTH_WHITELIST).permitAll()
				.anyRequest().authenticated()
				.and()
				.exceptionHandling().authenticationEntryPoint(authenticationEntryPointJwt)
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authenticationProvider(authenticationProvider());

		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
