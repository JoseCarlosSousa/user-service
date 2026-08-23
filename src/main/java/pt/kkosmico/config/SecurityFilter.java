package pt.kkosmico.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pt.kkosmico.service.TokenService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 1. Recover the token from the Authorization header
        String token = this.recoverToken(request);
        
        if (token != null) {
            // 2. Validate the token and extract the user's login/email
            String login = tokenService.validateToken(token); // Adjust based on your method name in TokenService
            
            if (login != null) {
                // 3. Extract the role from the token
                String role = tokenService.getRoleFromToken(token); // Adjust based on your method name in TokenService
                
                // 4. Create authority with the required ROLE_ prefix for hasAnyRole()
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(authority);
                
                // 5. Authenticate the user inside the Spring Security Context
                var authentication = new UsernamePasswordAuthenticationToken(login, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        // 6. Continue with the filter chain execution
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
