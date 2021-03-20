package com.ss.utopia.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.models.Airport;
import com.ss.utopia.repositories.AirportRepository;

@Service
public class AirportService {

	@Autowired
	private AirportRepository airportRepository;

	public List<Airport> findAll() {
		return airportRepository.findAll();
	}

}