package com.ss.utopia;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.Map;

import javax.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ss.utopia.exceptions.AirplaneAlreadyInUseException;
import com.ss.utopia.exceptions.AirplaneNotFoundException;
import com.ss.utopia.exceptions.FlightNotFoundException;
import com.ss.utopia.exceptions.RouteNotFoundException;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.ErrorMessage;
import com.ss.utopia.services.FlightService;

@RestController
@RestControllerAdvice
@RequestMapping("/flights")
public class FlightController {
	
	@Autowired
	private FlightService flightService;

	@GetMapping("/health")
	public ResponseEntity<Object> health() {
		return new ResponseEntity<>("\"status\": \"up\"", HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Object> findAll() {
		List<Flight> allFlights = flightService.findAll();
		return !allFlights.isEmpty() 
			? new ResponseEntity<>(allFlights, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("{flightId}")
	public ResponseEntity<Flight> findById(@PathVariable String flightId) 
	throws FlightNotFoundException {
		Integer formattedFlightId = Integer.parseInt(flightId);
		Flight flight = flightService.findById(formattedFlightId);
		return new ResponseEntity<>(flight, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Flight> create(@RequestBody @Valid Flight flight) 
	throws AirplaneAlreadyInUseException, RouteNotFoundException, 
	AirplaneNotFoundException, IllegalArgumentException {
		Flight newFlight = flightService.insert(
			flight.getFlightRoute().getRouteId(),
			flight.getFlightAirplane().getAirplaneId(),
			flight.getFlightDepartureTime(),
			flight.getFlightSeatingId(),
			flight.getFlightDuration(),
			flight.getFlightStatus()
		);
		
		return new ResponseEntity<>(newFlight, HttpStatus.CREATED);
	}

	@PostMapping("/search")
	public ResponseEntity<Object> findBySearchAndFilter(@RequestBody Map<String, String> filterMap) {
		List<Flight> flights = flightService.findBySearchAndFilter(filterMap);
		return !flights.isEmpty() 
			? new ResponseEntity<>(flights, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping
	public ResponseEntity<Flight> update(@RequestBody @Valid Flight flight) 
	throws AirplaneAlreadyInUseException, FlightNotFoundException, 
	RouteNotFoundException, AirplaneNotFoundException, IllegalArgumentException {
		Flight newFlight = flightService.update(
			flight.getFlightId(),
			flight.getFlightRoute().getRouteId(),
			flight.getFlightAirplane().getAirplaneId(),
			flight.getFlightDepartureTime(),
			flight.getFlightSeatingId(),
			flight.getFlightDuration(),
			flight.getFlightStatus()
		);
		return new ResponseEntity<>(newFlight, HttpStatus.OK);
	}
	
	@DeleteMapping("{flightId}")
	public ResponseEntity<Object> deleteById(@PathVariable String flightId) 
	throws FlightNotFoundException {
		String deleteInformation = flightService.deleteById(Integer.parseInt(flightId));
		return new ResponseEntity<>(deleteInformation, HttpStatus.ACCEPTED);
	}
	
	
	// Exception Handling
	// ========================================================================
	@ExceptionHandler(AirplaneAlreadyInUseException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Object> airplaneAlreadyInUseException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.CONFLICT
		);
	}

	@ExceptionHandler(FlightNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> flightNotFoundException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.NOT_FOUND
		);
	}

	@ExceptionHandler(AirplaneNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> airplaneNotFoundException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.NOT_FOUND
		);
	}

	@ExceptionHandler(RouteNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> routeNotFoundException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.NOT_FOUND
		);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> illegalArgumentException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.BAD_REQUEST
		);
	}

	@ExceptionHandler(ConnectException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ResponseEntity<Object> invalidConnection() {
		return new ResponseEntity<>(
			new ErrorMessage("Service temporarily unavailabe."), 
			HttpStatus.SERVICE_UNAVAILABLE
		);
	}

	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> invalidMessage() {
		return new ResponseEntity<>(
			new ErrorMessage("Invalid HTTP message content."), 
			HttpStatus.BAD_REQUEST
		);
	}

	@ExceptionHandler(NumberFormatException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> invalidParameters(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.BAD_REQUEST
		);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> IllegalArgumentException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.BAD_REQUEST
		);
	}

	@ExceptionHandler(SQLException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ResponseEntity<Object> invalidSQL() {
		return new ResponseEntity<>(
			new ErrorMessage("Service temporarily unavailabe."), 
			HttpStatus.SERVICE_UNAVAILABLE
		);
	}
}
