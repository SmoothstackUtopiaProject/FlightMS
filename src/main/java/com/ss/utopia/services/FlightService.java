package com.ss.utopia.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exceptions.AirplaneAlreadyInUseException;
import com.ss.utopia.exceptions.AirplaneNotFoundException;
import com.ss.utopia.exceptions.FlightNotFoundException;
import com.ss.utopia.exceptions.RouteNotFoundException;
import com.ss.utopia.filters.FlightFilters;
import com.ss.utopia.models.Airplane;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.Route;
import com.ss.utopia.repositories.FlightRepository;
import com.ss.utopia.timeformatting.FlightTimeFormatter;

@Service
public class FlightService {

	private static final Integer MINIMUM_AIRPLANE_NOFLIGHT_HOURS = 2;

	@Autowired
	private FlightRepository flightRepository;

	// Find All
	public List<Flight> findAll() {
		return flightRepository.findAll();		
	}
	
	// Find By ID
	public Flight findById(Integer id) throws FlightNotFoundException {
		Optional<Flight> optionalFlight = flightRepository.findById(id);
		if(!optionalFlight.isPresent()) {
			throw new FlightNotFoundException("No Flight with ID: " + id + " exist.");
		}
		return optionalFlight.get();
	}
	
	// Search & Filter
	public List<Flight> findBySearchAndFilter(Map<String, String> filterMap) {
		List<Flight> flights = findAll();
		if(!filterMap.keySet().isEmpty()) {
			flights = FlightFilters.apply(flights, filterMap);
		}
		return flights;
	}

	// Insert
	public Flight insert(Integer routeId ,Integer airplaneId , String dateTime, 
	Integer seatingId, Integer duration, String status) throws AirplaneAlreadyInUseException, 
	RouteNotFoundException, AirplaneNotFoundException {

		Optional<Route> optionalRoute = flightRepository.findRouteById(routeId);
		if(!optionalRoute.isPresent()) {
			throw new RouteNotFoundException("No Route with ID: " + routeId + " exist.");
		}
		Route route = optionalRoute.get();
		
		Optional<Airplane> optionalAirplane = flightRepository.findAirplaneById(airplaneId);
		if(!optionalAirplane.isPresent()) {
			throw new AirplaneNotFoundException("No Airplane with ID: " + airplaneId + " exist.");
		}
		Airplane airplane = optionalAirplane.get();
		
		List<Flight> flightsWithAirplaneId = flightRepository.findFlightsByAirplaneId(airplaneId)
			.stream().filter(i -> 
			Math.abs(Duration.between(
					LocalDateTime.parse(dateTime, FlightTimeFormatter.getInstance()), 
					LocalDateTime.parse(i.getFlightDepartureTime(), FlightTimeFormatter.getInstance())
				).toHours()) < MINIMUM_AIRPLANE_NOFLIGHT_HOURS
			)
			.collect(Collectors.toList());

		if(!flightsWithAirplaneId.isEmpty()) {
			throw new AirplaneAlreadyInUseException(
				"Airplane with id: " + airplaneId +" already has flights within two hours of what you are trying to create"
			);
		}

		return flightRepository.save(new Flight(route, airplane, dateTime, seatingId, duration, status));
	}
	
	// Update
	public Flight update(Integer id, Integer routeId, Integer airplaneId, String dateTime, 
	Integer seatingId, Integer duration, String status) throws AirplaneAlreadyInUseException, 
	FlightNotFoundException, RouteNotFoundException, AirplaneNotFoundException {

		Optional<Flight> optionalFlight = flightRepository.findById(id);
		if(!optionalFlight.isPresent()) {
			throw new FlightNotFoundException("No flight with the id: " + id + " exists!");
		}

		Optional<Route> optionalRoute = flightRepository.findRouteById(routeId);
		if(!optionalRoute.isPresent()) {
			throw new RouteNotFoundException("No Route with ID: " + routeId + " exist.");
		}
		Route route = optionalRoute.get();
		
		Optional<Airplane> optionalAirplane = flightRepository.findAirplaneById(airplaneId);
		if(!optionalAirplane.isPresent()) {
			throw new AirplaneNotFoundException("No Airplane with ID: " + airplaneId + " exist.");
		}
		Airplane airplane = optionalAirplane.get();
		
		List<Flight> flightsWithAirplaneId = flightRepository.findFlightsByAirplaneId(airplaneId)
				.stream().filter(i -> 
				(Math.abs(Duration.between(
						LocalDateTime.parse(dateTime, FlightTimeFormatter.getInstance()), 
						LocalDateTime.parse(i.getFlightDepartureTime(), FlightTimeFormatter.getInstance())
					).toHours()) < MINIMUM_AIRPLANE_NOFLIGHT_HOURS
				) && !i.getFlightId().equals(id))
				.collect(Collectors.toList());
		
		if(!flightsWithAirplaneId.isEmpty()) {
			throw new AirplaneAlreadyInUseException(
				"Airplane with id: " + airplaneId +" already has flights within two hours of what you are trying to create"
			);
		}
		
		return flightRepository.save(new Flight(id, route, airplane, dateTime, seatingId, duration, status));
	}	

	// Delete by ID
	public String deleteById(Integer id) throws FlightNotFoundException {
		Optional<Flight> optionalFlight = flightRepository.findById(id);
		if(!optionalFlight.isPresent()) {
			throw new FlightNotFoundException("No flight with the id: " + id + " exists!");
		}
		flightRepository.deleteById(id);
		return "Flight with ID: " + id + " was deleted.";
	}
}