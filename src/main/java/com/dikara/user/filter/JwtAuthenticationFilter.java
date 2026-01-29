package com.dikara.user.filter;

import com.dikara.user.util.JwtPrincipal;
import com.dikara.user.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "security.jwt.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
        Claims claims = jwtUtil.validate(token);

        String username = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);

        for(String role:roles){
            System.out.println("Roles adalah : "+ role);
        }

            String resultRoles = String.join(", ", roles);

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .toList();
            JwtPrincipal principal = new JwtPrincipal(
                    claims.get("userId", String.class),
                    username,
                    roles
            );

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        authorities
                );


            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (ExpiredJwtException e) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "TOKEN_EXPIRED", "JWT token has expired");
            return;

        } catch (JwtException e) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "INVALID_TOKEN", "JWT token is invalid");
            return;
        }
        catch (AccessDeniedException e) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "ACCESS_DENIED", "access denied");
            return;
        }


            chain.doFilter(request, response);


    }

    private void writeError(
            HttpServletResponse response,
            int status,
            String code,
            String message
    ) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");

        response.getWriter().write("""
            {
              "status": %d,
              "error": "%s",
              "message": "%s"
            }
        """.formatted(status, code, message));
    }
}
