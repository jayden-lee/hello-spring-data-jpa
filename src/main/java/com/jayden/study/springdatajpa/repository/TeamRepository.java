package com.jayden.study.springdatajpa.repository;

import com.jayden.study.springdatajpa.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
