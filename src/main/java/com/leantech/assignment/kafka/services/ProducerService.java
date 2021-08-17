package com.leantech.assignment.kafka.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.leantech.assignment.kafka.models.Bookings;

@Service
public class ProducerService {

	private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);	
	private static final String TOPIC = "bookings";
	
	@Autowired
	private KafkaTemplate<String, Bookings> kafkaTemplate;
	
	
    public String send(Bookings booking) {
    	
    	String result = "";
    	
    	try {
    		ListenableFuture<SendResult<String, Bookings>> future = kafkaTemplate.send(TOPIC, booking);
            future.addCallback(new ListenableFutureCallback<SendResult<String, Bookings>>() {

                @Override
                public void onSuccess(final SendResult<String, Bookings> booking) {
                	logger.info("sent message= " + booking.getRecordMetadata().offset());
                	//Send email succesfull
                }

                @Override
                public void onFailure(final Throwable throwable) {
                	logger.error("unable to send message= " + booking, throwable);
                }

    		
            });
            
            if(future.isCancelled()) {
            	result = "El envío de la información a Kafka no se completó, revise la conexión";
            }
  
    	}catch(Exception e) {
    		
    		return result = "El envío de la información a Kafka no se completó debido a la excepción :\n" + e.toString();
    	}
    	
        return result;
    }
	
}

