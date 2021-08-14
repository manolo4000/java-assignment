package com.leantech.assignment.kafka.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leantech.assignment.kafka.models.Bookings;

public interface BookingsDAO extends JpaRepository<Bookings, Integer> {

}
