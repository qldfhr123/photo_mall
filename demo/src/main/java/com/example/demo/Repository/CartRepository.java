package com.example.demo.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Entity.Carts;


public interface CartRepository extends JpaRepository<Carts, Integer>{
	// 네이밍 규칙 준수 하기
	List<Carts> findByMemberId(int memberId);
	Carts findByMemberIdAndItemId(int memberId, int itemId);
	void deleteByMemberId(int memberId);
}
