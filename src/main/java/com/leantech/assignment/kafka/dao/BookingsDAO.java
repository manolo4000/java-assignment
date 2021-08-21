package com.leantech.assignment.kafka.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.leantech.assignment.kafka.models.Bookings;
import com.leantech.assignment.kafka.models.Users;

public interface BookingsDAO extends JpaRepository<Bookings, Integer> {

	@Query(value="select * from bookings b where b.username = :username LIMIT 1", nativeQuery=true)
    Users findByUsername(@Param("username") String username);

}
