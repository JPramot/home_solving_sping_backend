package com.spring.home_solver.security.jwt;

import com.spring.home_solver.security.service.UserDetailImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    private final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        logger.debug("AuthTokenFilter call for api: {}",request.getRequestURI());

        String path = request.getServletPath();
        if(path.equals("/api/auth/login") || path.equals("/api/auth/register")){
            filterChain.doFilter(request,response);
            return;
        }

        try{
            String jwt =jwtUtils.getJwtFromHeader(request);
            if(jwt!=null && jwtUtils.validateJwt(jwt)) {
                String username = jwtUtils.getUsernameFromToken(jwt);

                UserDetailImpl userDetails =(UserDetailImpl) userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }catch (Exception exc) {
            logger.error("cannot set user authentication: {}",exc.getMessage());
        }
        filterChain.doFilter(request, response);

    }
}
