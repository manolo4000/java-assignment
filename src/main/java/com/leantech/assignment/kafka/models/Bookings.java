package com.leantech.assignment.kafka.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bookings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Date checkin;
	private Date checkout;
	
	@ManyToOne
	@JoinColumn(name = "holderId", insertable = false, updatable = false)
	private Holders holder;
	private Integer holderId;
	
	private Integer totalDays;
	private Integer people;
	private Integer rooms;
	private Integer minors;
	
	
}
