package com.jayden.study.springdatajpa.repository;

import com.jayden.study.springdatajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
