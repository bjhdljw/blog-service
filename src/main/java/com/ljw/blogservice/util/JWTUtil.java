package com.ljw.blogservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Map;

public class JWTUtil {

    private static final String key = "ljw";

    public static String encode(Map<String, Object> param, String ip) {
        String saltWithIp = key;
        if(ip != null) {
            saltWithIp += ip;
        }
        JwtBuilder jwtBuilder = Jwts.builder().signWith(SignatureAlgorithm.HS256, saltWithIp);
        jwtBuilder = jwtBuilder.setClaims(param);
        String token = jwtBuilder.compact();
        return token;
    }

    public static Map<String, Object> decode(String token, String ip) {
        String saltWithIp = key;
        if(ip != null) {
            saltWithIp += ip;
        }
        Claims claims = Jwts.parser().setSigningKey(saltWithIp).parseClaimsJws(token).getBody();
        return claims;
    }

}
