package com.example.demo.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.Entity.Member;
import com.example.demo.Repository.MemberRepository;
import com.example.demo.Service.JwtService;
import com.example.demo.Service.JwtServiceImpl;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AccountController {
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	JwtService jwtService;
	
	@PostMapping("/api/account/login")
	public ResponseEntity login(@RequestBody Map<String, String> parms,
								HttpServletResponse res) {
		
		Member member = memberRepository.findByEmailAndPassword(parms.get("email"),parms.get("password"));
		if(member != null) {
			int id = member.getId();
			String token = jwtService.getToken("id",id);
			
			Cookie cookie = new Cookie("token", token);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			cookie.setSecure(false); // HTTPS 아니라면 false
			res.addCookie(cookie);
//		    res.addHeader("Set-Cookie", String.format(
//		            "token=%s; Path=/; HttpOnly; SameSite=None; Secure", token
//		        ));

			
			
			return new ResponseEntity<>(id,HttpStatus.OK);
		}		
		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/api/account/check")
	public ResponseEntity check(@CookieValue(value = "token", required = false)String token) {
//		System.out.println("토큰: "+token);
		Claims claims = jwtService.getClaims(token);
		/*토큰은 있음 하지만 로그인 체크 로직에서 오류가 있을 가능성 큼*/
		
		if(claims != null) {
			int id = Integer.parseInt(claims.get("id").toString());
			
			return new ResponseEntity<>(id, HttpStatus.OK);
		}
		
	
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@GetMapping("/api/account/logout")
	public ResponseEntity<?> logout(HttpServletResponse response) {
	    Cookie cookie = new Cookie("token", null);
	    
	    cookie.setPath("/");
	    cookie.setMaxAge(0); // 즉시 만료
	    response.addCookie(cookie);
	    return ResponseEntity.ok("로그아웃 완료");
	}

}
