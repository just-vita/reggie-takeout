package com.learn.reggie.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtil {
    private static final String jwtToken = "myCode";

    public static String createToken(String userId){
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        JwtBuilder builder = Jwts.builder();
        Date date = new Date(System.currentTimeMillis() + 10000000);
        log.info("token的过期时间为： " + new SimpleDateFormat("yy-MM-dd hh:mm:ss").format(date));
        builder.signWith(SignatureAlgorithm.HS256, jwtToken)
                .setClaims(claims)
                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000));
                .setExpiration(date);
        return builder.compact();
    }

    public static Map<String,Object> checkToken(String token){
        // 去除token中的双引号，偷懒
        token = token.substring(1, token.length() - 1);
        Jwt parse = null;
        try {
            parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
        } catch (Exception e) {
            log.error("Jwt验证失败");
            e.printStackTrace();
            return null;
        }
    }

}
