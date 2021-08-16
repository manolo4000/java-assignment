package com.leantech.assignment.kafka.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leantech.assignment.kafka.dao.BookingsDAO;
import com.leantech.assignment.kafka.models.Bookings;
import com.leantech.assignment.kafka.services.ProducerService;

@RestController
@RequestMapping("controller")
public class BookingsController {
	
	@Autowired
	private BookingsDAO bookingsDao;
	
	@Autowired
	private ProducerService producer;
	
	@PostMapping("/registrar-reserva")
	public String registrar(@RequestBody Bookings booking) {
		
		producer.send(booking);
		//bookingsDao.save(booking);
		
		return "Publish successful";
		
	}
	
	@GetMapping("/consultar-reserva")
	public String consultar() {
		return "Hola mundo";
	}
}
