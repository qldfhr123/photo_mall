package com.example.demo.Service;

import io.jsonwebtoken.Claims;

public interface JwtService {

	String getToken(String key, Object value);
	
	Claims getClaims(String token);
		
	/** 요청한 사용자 맞는지 검증 */  
	boolean isValid(String token);
	
	int getId(String token);
}
