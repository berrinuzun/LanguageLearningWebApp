package com.app.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

import com.app.service.CustomSuccessHandler;
import com.app.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	CustomSuccessHandler customSuccessHandler;

	@Bean
	public static PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
		
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		
		http.csrf( c -> c.disable() )
		
				.authorizeHttpRequests(
						
								request -> request.requestMatchers("/admin-page").hasAuthority("admin")
											      	.requestMatchers("/learner-page").hasAuthority("learner")
											      	.requestMatchers("/instructor-page").hasAuthority("instructor")
											      	.requestMatchers("/registration", "/css/**", "/images/**").permitAll()
											      	.requestMatchers("/login").permitAll()
											      	.requestMatchers("/password-request").permitAll()
											      	.requestMatchers("/reset-password").permitAll()
											      	.anyRequest().authenticated()
						
						)
				
				.formLogin(
						
								form -> form.loginPage("/login").loginProcessingUrl("/login")
													.successHandler(customSuccessHandler).permitAll()
						
						)
				
				.logout(
						
								form -> form.invalidateHttpSession(true).clearAuthentication(true)
											.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
											.logoutSuccessUrl("/login?logout").permitAll()
						
						);
		
		return http.build();
		
		
	}
	
	
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
		
		
	}
}