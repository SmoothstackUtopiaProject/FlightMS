package com.ss.utopia.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.models.Airplane;
import com.ss.utopia.models.AirplaneType;
import com.ss.utopia.repositories.AirplaneRepository;

@Service
public class AirplaneService {
	
	@Autowired
	private AirplaneRepository airplaneRepository;

	public List<Airplane> findAll() {
		return airplaneRepository.findAll();
	}

	public List<AirplaneType> findAllAirplaneTypes() {
		return airplaneRepository.findAllAirplaneTypes();
	}
	
}