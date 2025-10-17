package com.example.demo.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Entity.Member;


public interface MemberRepository extends JpaRepository<Member, Integer>{
	// 네이밍 규칙 준수 하기
	Member findByEmailAndPassword(String email, String password);
}
