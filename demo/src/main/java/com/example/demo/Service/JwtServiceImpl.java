package com.example.demo.Service;

import java.security.Key;
import java.util.*;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import jakarta.xml.bind.DatatypeConverter;

@Service("JwtService")
public class JwtServiceImpl implements JwtService {

	private String secretKey = "qwerasdad123e564123@#$@$@21231231245124142412sadfsf3e3e3e1322313a123e1sdfasdgfsfghfsdghrth";

	@Override
	public String getToken(String key, Object value) {

		Date expTime = new Date();
		expTime.setTime(expTime.getTime() + 1000 * 60 * 5);
		byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
		Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());

		Map<String, Object> headerMap = new HashMap();
		headerMap.put("typ", "JWT");
		headerMap.put("alg", "HS256");

		Map<String, Object> claims = new HashMap();
		claims.put(key, value); 
		JwtBuilder bulider = Jwts.builder().setHeader(headerMap).setClaims(claims).setExpiration(expTime)
				.signWith(signKey, SignatureAlgorithm.HS256);

		return bulider.compact();
	}

	@Override
	public Claims getClaims(String token) {
		if (token != null && !"".equals(token)) {
			try {
				byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
				Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());
				Claims claims = Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token).getBody();
				return claims;
			} catch (ExpiredJwtException e) {
				// 만료됨
			} catch (JwtException e) {
				// 유효하지않음
			}

		}
		return null;
	}

	@Override
	public boolean isValid(String token) {
		
		return this.getClaims(token) != null;
	}	

	@Override
	public int getId(String token) {
		Claims claims = this.getClaims(token);
		
		if(claims != null) {
			return Integer.parseInt(claims.get("id").toString());
		}
		
		return 0;
	}

}
