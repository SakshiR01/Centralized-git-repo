package com.gemini.Contripoint.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gemini.Contripoint.model.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtTokenUtils {

    private static final String USER_ROLE = "user_role";
    private static final String ID = "id";
    private static final String USER_EMAIL = "user_email";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.issuer}")
    private String jwtIssuer;

    public String generateToken(final UserInfo user) {
        Algorithm algorithm = Algorithm.HMAC512(secret);
        return JWT.create().withIssuer(jwtIssuer)
                .withSubject(user.getName())
                .withClaim(ID, user.getUserId())
                .withClaim(USER_EMAIL, user.getEmail())
                .sign(algorithm);
    }


    public UserInfo parseToken(final String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(jwtIssuer).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            UserInfo user = new UserInfo();
            user.setUserId(decodedJWT.getClaim(ID).toString());
            user.setEmail(decodedJWT.getClaim(USER_EMAIL).asString());
            return user;
        } catch (JWTVerificationException e) {
            return null;
        }
    }

}
