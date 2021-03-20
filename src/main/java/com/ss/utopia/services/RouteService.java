package com.ss.utopia.services;

import java.util.Map;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exceptions.RouteNotFoundException;
import com.ss.utopia.filters.RouteFilters;
import com.ss.utopia.models.Route;
import com.ss.utopia.repositories.RouteRepository;


@Service
public class RouteService {

	@Autowired
	private RouteRepository routeRepository;

	public List<Route> findAll() {
		return routeRepository.findAll();
	}
	
	public Route findById(Integer id) throws RouteNotFoundException {
		Optional<Route> optionalRoute = routeRepository.findById(id);
		if(!optionalRoute.isPresent()) {
			throw new RouteNotFoundException("No Route with ID: " + id + " exists.");
		}
		return optionalRoute.get();
	}

	public List<Route> findBySearchAndFilter(Map<String, String> filterMap) {
		List<Route> routes = findAll();
		if(!filterMap.keySet().isEmpty()) {
			routes = RouteFilters.apply(routes, filterMap);
		}
		return routes;
	}
}