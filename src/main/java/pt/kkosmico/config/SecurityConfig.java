package pt.kkosmico.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private SecurityFilter securityFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
	            .csrf(csrf -> csrf.disable())
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .authorizeHttpRequests(authorize -> authorize
	                    // Allow CORS Pre-Flight requests
	                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()

	                    // Restrict GET users endpoint to ADMIN and MANAGER roles only
	                    .requestMatchers(HttpMethod.GET, "/api/users").hasAnyRole("ADMIN", "MANAGER")
	                    
	                    // 🌟 ADD THIS BLOCK: Allow any authenticated user (USER, ADMIN, MANAGER) to access their own profile
	                    .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
	                    .requestMatchers(HttpMethod.PUT, "/api/users/me").authenticated()
	                    
	                    // Public endpoints for registration and login
	                    .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
	                    .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
	                    
	                    // Any other request must be authenticated
	                    .anyRequest().authenticated()
	            )

	            // 🌟 ADD THIS LINE HERE: Apply our custom JWT filter before UsernamePasswordAuthenticationFilter
	            .addFilterBefore(securityFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
