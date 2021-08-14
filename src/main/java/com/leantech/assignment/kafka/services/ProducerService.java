package com.leantech.assignment.kafka.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

	private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);	
	private static final String TOPIC = "bookings";
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	
	//Sends the message to kafka
	public void send(String message) {
		logger.info(String.format("Producer is sending the message", message ));
		kafkaTemplate.send(TOPIC, message);
	}
}
