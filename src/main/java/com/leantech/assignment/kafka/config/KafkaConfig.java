package com.leantech.assignment.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.leantech.assignment.kafka.models.Bookings;

@EnableKafka
@Configuration
public class KafkaConfig {
	
	

	@Bean
	public NewTopic compactTopicExample() {
		return TopicBuilder.name("bookings")
				.partitions(1)
				.replicas(1)
				.compact()
				.build();
	  }
	
	/*@Bean
    public NewTopic bookingsTopic() {
		return new NewTopic("bookings", 1, (short) 1);
    	
    }*/
	
	@Bean
    public ProducerFactory<String, Bookings> producerFactory() {
		
		
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Bookings> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    
    
    @Bean
    public ConsumerFactory<String, Bookings> consumerFactory(){
    	Map<String, Object> config = new HashMap<>();
    	config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    	config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
    	config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    	config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    	
    	return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(Bookings.class));
    }
    
    
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Bookings> concurrentKafkaListenerContainerFactoryJson() {
    	ConcurrentKafkaListenerContainerFactory<String, Bookings> factory = new ConcurrentKafkaListenerContainerFactory<>();
    	factory.setConsumerFactory(consumerFactory());
    	return factory;
    }
    
}
