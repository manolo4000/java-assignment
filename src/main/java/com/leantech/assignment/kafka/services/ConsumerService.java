package com.leantech.assignment.kafka.services;

import java.text.SimpleDateFormat;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.leantech.assignment.kafka.dao.BookingsDAO;
import com.leantech.assignment.kafka.models.Bookings;

@Service
public class ConsumerService {

	private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);
	
	@Autowired
	private BookingsDAO bookingsDAO;
	
	@Autowired
	private EmailService emailService;
	
	@KafkaListener(topics = "bookings", groupId = "group_id", containerFactory = "concurrentKafkaListenerContainerFactoryJson")
	public void consume(Bookings booking) {
		logger.info(String.format("Consumer receive the message " + booking ));
		
		bookingsDAO.save(booking);
		
		String pattern = "dd/MM/yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		
		
		String To = booking.getTitularReservaUser().getUsername();
		String subject = "Confirmación de reserva";
    	Context ctx = new Context();
    	ctx.setVariable("idBooking", ""+booking.getId());
		ctx.setVariable("fullname", booking.getTitularReservaUser().getName() + " " + booking.getTitularReservaUser().getLastname());
    	ctx.setVariable("checkin", simpleDateFormat.format(booking.getFechaIngreso()));
    	ctx.setVariable("checkout",simpleDateFormat.format(booking.getFechaSalida())); 
    	ctx.setVariable("rooms", ""+booking.getNumeroHabitaciones());
    	
		try {
			emailService.sendMailConfirmation(To,subject,ctx);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
