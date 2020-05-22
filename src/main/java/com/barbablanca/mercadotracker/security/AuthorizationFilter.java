package com.barbablanca.mercadotracker.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class AuthorizationFilter extends OncePerRequestFilter {

    Logger log = LoggerFactory.getLogger(AuthorizationFilter.class);

    private final JWTVerifier verifier;

    Environment env;

    public AuthorizationFilter(Environment env) {
        this.env = env;
        Algorithm algorithm = Algorithm.HMAC256(Objects.requireNonNull(env.getProperty("JWT_SECRET")));
        verifier = JWT.require(algorithm).build();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader("authorization");

        SecurityContextHolder.clearContext();

         if (token != null) {

             try {
                 DecodedJWT decodedJWT = verifier.verify(token);

                 PrincipalCredentials principal = new PrincipalCredentials(
                         decodedJWT.getClaim("userId").asInt(),
                         decodedJWT.getClaim("username").asString()
                 );

                 log.info("Authorized user " + principal.getId() + ": " + principal.getName());

                 SecurityContextHolder.getContext()
                         .setAuthentication(new UsernamePasswordAuthenticationToken(principal, token, null));


                 Algorithm algorithm = Algorithm.HMAC256("secret");

                 Date expiresAt = new Date();
                 expiresAt.setTime(expiresAt.getTime() + 900000);

                 String refreshToken = JWT.create()
                         .withExpiresAt(expiresAt)
                         .withClaim("userId", principal.getId())
                         .withClaim("username", principal.getName())
                         .sign(algorithm);

                 response.addHeader("authorization", refreshToken);
             }
             catch (Exception e) {
                 log.error("An error ocurred trying to authorize a request " +  e.getMessage());
             }
        }

        filterChain.doFilter(request, response);
    }
}
