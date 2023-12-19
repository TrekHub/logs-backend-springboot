package com.trekhub.logs.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static  final String SECRET_KEY = "e286efee249497192f1838cf31b06033b5b92118450f6dbf5a0cf4a971c708a3";

    public String extractUsername(String jwtToken) {
        return  extractClaim(jwtToken, Claims::getSubject);
    }


    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);

    }

    //generate a jwt token
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
        return Jwts.builder().setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }


    //validate a token
    public  boolean isTokenIsValid(String jwtToken, UserDetails userDetails){
        final String username = extractUsername(jwtToken);

        return  (username.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);
    }



    public  boolean isTokenExpired(String jwtToken){
        return  extractExpiration(jwtToken).before(new Date());
    }

    public Date extractExpiration(String jwtToken){
        return  extractClaim(jwtToken, Claims::getExpiration);
    }
    private Claims extractAllClaims(String jwtToken){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJwt(jwtToken)
                .getBody();
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
