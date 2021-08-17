package com.leantech.assignment.kafka.controllers;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leantech.assignment.kafka.dao.BookingsDAO;
import com.leantech.assignment.kafka.models.Bookings;
import com.leantech.assignment.kafka.models.Users;
import com.leantech.assignment.kafka.services.JWTUtilityService;
import com.leantech.assignment.kafka.services.ProducerService;
import com.leantech.assignment.kafka.services.UserService;

@RestController
@RequestMapping("controller")
public class BookingsController {
	
	@Autowired
	private BookingsDAO bookingsDao;
	
	@Autowired
	private ProducerService producer;
	
    @Autowired
    private JWTUtilityService jwtUtility;

    @Autowired
    private UserService userService;
	
	@PostMapping("/registrar-reserva")
	public ResponseEntity<List<String>> registrar(@RequestBody Bookings booking, HttpServletRequest httpServletRequest) {
		
		Users loggedin = userService.getUserByToken(httpServletRequest);
		List<String> Errors = new ArrayList<String>();
		
		boolean entrada = false;
		boolean salida = false;
		
		if(loggedin == null) {
			Errors.add("Usuario no reconocido o registrado, reingrese o contacte a un administrador");
		}
		
		if(booking.getId() != null){
			Errors.add("No puede asignar un Id a la reserva");
		}
		
		if(booking.getFechaIngreso() == null){
			Errors.add("No registró fecha de ingreso");
		}else {
			entrada = true;
		}
		
		if(booking.getFechaSalida() == null){
			Errors.add("No registró fecha de salida");
		}else {
			salida = true;
		}
		
		if(booking.getNumeroPersonas() == null) {
			Errors.add("No registró la cantidad de personas(adultos)");
		} else if (booking.getNumeroPersonas() <= 0 ) {
			
			Errors.add("Como mínimo debe asistir un adulto");
			if(booking.getNumeroPersonas() < 0 ) {
				Errors.add("No puede poner números negativos en el campo numeroPersonas");
			}
		}
		
		if(booking.getNumeroMenores() == null) {
			booking.setNumeroMenores(0);
		}else if(booking.getNumeroMenores() < 0) {
			Errors.add("No puede poner números negativos en el campo numeroMenores");
		}
		
		if(booking.getTotalDias() != null) {
			Errors.add("No puede asignar la  cantidad de días (totalDias), estas se calculan automáticamente a partir de (fechaIngreso) y (fechaSalida)");
		}
		
		if(booking.getNumeroHabitaciones() == null) {
			Errors.add("No registró la cantidad de personas(adultos)");
		} else if (booking.getNumeroHabitaciones() <= 0 ) {
			Errors.add("Como mínimo debe seleccionar una habitación");
			if(booking.getNumeroHabitaciones() < 0 ) {
				Errors.add("No puede poner números negativos en el campo numeroHabitaciones");
			}
		}
		
		if(booking.getTitularReserva() != null || booking.getTitularReservaUser() != null) {
			Errors.add("No puede asignar un titular de la reserva este se asigna automáticamente");
		}
	
		
		if(entrada && salida) {
			
			Date checkin = booking.getFechaIngreso();
			Date checkout = booking.getFechaSalida();
			Integer TotalDays = 0;
			
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Calendar calendar = Calendar.getInstance();
			
			try {
				calendar.setTime(checkin);
				calendar.add(Calendar.HOUR_OF_DAY, 5);
				checkin = formatter.parse(formatter.format(calendar.getTime()));

				calendar.setTime(checkout);
				calendar.add(Calendar.HOUR_OF_DAY, 5);
				checkout =  formatter.parse(formatter.format(calendar.getTime()));
				
				boolean calculate = false;
				if (checkin.compareTo(checkout) < 0) {
					//Checkin va antes que checkout, todo bien
					calculate = true;
				}else if(checkin.compareTo(checkout) == 0) {
					//Es la misma fecha, no puede ser
					Errors.add("La fecha de ingreso y de salida no puede ser la misma");
				} else {
					//Checkin va después que checkout 
					Errors.add("La fecha de ingreso debe ser antes que la fecha salida");
				}
				
				if(calculate) {
					Date d1 = checkin;
					Date d2 = checkout;
					LocalDateTime date1 = d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					LocalDateTime date2 = d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					
					TotalDays = (int) (long) Duration.between(date1, date2).toDays();
				}

			} catch (Exception e) {
				Errors.add("Error de Excepción : \n" + e.toString());
			}
			
			if(Errors.isEmpty()) {
				booking.setFechaIngreso(checkin);
				booking.setFechaSalida(checkout);
				booking.setTotalDias(TotalDays);
				booking.setTitularReserva(loggedin.getId());
				booking.setTitularReservaUser(loggedin);
				//bookingsDao.save(booking);
				
				String ConnectionStatus = producer.send(booking);
				
				if(ConnectionStatus == "") {
					//Enviar email satisfactorio
					Errors.add("Registro satisfactorio");
					return new ResponseEntity<List<String>>(Errors, HttpStatus.OK);
				}else {
					Errors.add(ConnectionStatus);
					return new ResponseEntity<List<String>>(Errors, HttpStatus.SERVICE_UNAVAILABLE);
				}
				
			}

		}
		
		
		return new ResponseEntity<List<String>>(Errors, HttpStatus.BAD_REQUEST);
		
	}
	
	@GetMapping("/consultar-reserva")
	public String consultar() {
		return "Hola mundo";
	}
}
