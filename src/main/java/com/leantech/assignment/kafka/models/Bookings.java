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
	
	private Date fechaIngreso;
	private Date fechaSalida;
	
	@ManyToOne
	@JoinColumn(name = "titularReserva", insertable = false, updatable = false)
	private Users titularReservaUser;
	private Integer titularReserva;
	
	private Integer totalDias;
	private Integer numeroPersonas;
	private Integer numeroHabitaciones;
	private Integer numeroMenores;
	
	
}
