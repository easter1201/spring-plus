package org.example.expert.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException{
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            try{
                String token = jwtUtil.substringToken(bearerToken);
                Claims claims = jwtUtil.extractClaims(token);

                Long userId = Long.parseLong(claims.getSubject());
                String email = claims.get("email", String.class);
                String role = claims.get("userRole", String.class);
                String nickName = claims.get("nickName", String.class);
                UserRole userRole = UserRole.of(role);

                List<GrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + userRole.name())
                );

                AuthUser authUser = new AuthUser(userId, email, userRole, nickName);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authUser, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e){
                log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
            } catch (Exception e){
                log.error("Internal server error", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }

        filterChain.doFilter(request, response);
    }
}
