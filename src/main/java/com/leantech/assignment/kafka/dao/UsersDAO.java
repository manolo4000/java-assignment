package com.leantech.assignment.kafka.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.leantech.assignment.kafka.models.Users;

public interface UsersDAO extends JpaRepository<Users, Integer> {

	@Query(value="select * from users e where e.username = :username LIMIT 1", nativeQuery=true)
    Users findByUsername(@Param("username") String username);

}
