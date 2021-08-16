package com.leantech.assignment.kafka.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

	private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);
	
	@KafkaListener(topics = "bookings", groupId = "group_id")
	public void consume(String message) {
		logger.info(String.format("Consumer receive the message", message ));
		
	}
}
