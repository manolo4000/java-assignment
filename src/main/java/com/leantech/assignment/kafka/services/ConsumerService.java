package com.leantech.assignment.kafka.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.leantech.assignment.kafka.dao.BookingsDAO;
import com.leantech.assignment.kafka.dao.UsersDAO;
import com.leantech.assignment.kafka.models.Bookings;
import com.leantech.assignment.kafka.models.Users;

@Service
public class ConsumerService {

	private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);
	
	@Autowired
	private BookingsDAO bookingsDAO;
	
	@Autowired
	private UsersDAO usersDAO;
	
	@KafkaListener(topics = "bookings", groupId = "group_id", containerFactory = "concurrentKafkaListenerContainerFactoryJson")
	public void consume(Bookings booking) {
		logger.info(String.format("Consumer receive the message " + booking ));
		
		bookingsDAO.save(booking);
		System.out.println("Enviar email a: " + booking.getTitularReservaUser().getUsername());
	}
}
