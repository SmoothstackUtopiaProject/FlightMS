package com.utopia.flight.service;

import java.net.ConnectException;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utopia.flight.exception.FlightNotFoundException;
import com.utopia.flight.model.Flight;
import com.utopia.flight.respository.FlightRespository;

@Service
public class FlightService {

	@Autowired
	FlightRespository flightRespository;

	public List<Flight> getAll() {
		List<Flight> all = flightRespository.findAllFlights();
		return all;
	}
	
    public Optional<Flight> findById(Integer id) {
        return flightRespository.findById(id);
    }

	public Flight insert(Flight flight) {
		return flightRespository.save(flight);
	}

	public Flight update(Flight flight) {
		return flightRespository.save(flight);
	}

	public void delete(Integer id) {
		flightRespository.deleteById(id);
	}

	// search for flights, given route id and date
	public List<Flight> search(String routeId, String date) {
		System.out.println(date);
		Integer routeIdToInt = Integer.parseInt(routeId);
		LocalDate stingToDate = LocalDate.parse(date);

		return flightRespository.searchFlightByRouteIdAndDate(routeIdToInt, stingToDate);
	}


}
