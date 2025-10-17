package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.Entity.Item;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.ItemRepository;
import com.example.demo.Service.JwtService;
import com.example.demo.Entity.Carts;

@RestController
public class CartController {

	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	JwtService jwtService;
	
	@GetMapping("/api/cart/items")
	public ResponseEntity getCartItems(@CookieValue(value = "token", required = false) String token) {
		
		if(!jwtService.isValid(token)) {
			// 만료된 엑세스
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		int memberId = jwtService.getId(token);
		List<Carts> carts = cartRepository.findByMemberId(memberId);
		
		// items 정보만 가져오는 
		List<Integer> itemsIds = carts.stream().map(Carts::getItemId).toList();
		List<Item> items = itemRepository.findByIdIn(itemsIds); 
		
		
		
		return new ResponseEntity<>(items, HttpStatus.OK);
	}
	
	

	@PostMapping("/api/cart/item/{itemId}")
	public ResponseEntity pushCartItem(
			@PathVariable("itemId") int itemId,
			@CookieValue(value = "token", required = false) String token
			) {

		if(!jwtService.isValid(token)) {
			// 만료된 엑세스
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		int memberId = jwtService.getId(token);
		Carts cart = cartRepository.findByMemberIdAndItemId(memberId, itemId);
		
		if (cart == null) {
			Carts newCart = new Carts();
			newCart.setMemberId(memberId);
			newCart.setItemId(itemId);
			cartRepository.save(newCart);
			
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}


}
