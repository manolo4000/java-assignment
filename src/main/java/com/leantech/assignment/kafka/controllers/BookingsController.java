package com.leantech.assignment.kafka.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leantech.assignment.kafka.models.Bookings;
import com.leantech.assignment.kafka.services.BookingsDAO;

@RestController
@RequestMapping("controller")
public class BookingsController {
	
	@Autowired
	BookingsDAO bookingsDao;
	
	@PostMapping("/registrar-reserva")
	public void registrar(@RequestBody Bookings booking) {
		bookingsDao.save(booking);
	}
	
}
