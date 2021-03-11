package com.ss.utopia.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exceptions.AirportNotFoundException;
import com.ss.utopia.exceptions.RouteAlreadyExistsException;
import com.ss.utopia.exceptions.RouteNotFoundException;
import com.ss.utopia.models.Airport;
import com.ss.utopia.models.Route;
import com.ss.utopia.repositories.RouteRepository;


@Service
public class RouteService {
	
	@Autowired
	private AirportService airportService;

	@Autowired
	private RouteRepository routeRepository;

	public List<Route> findAll() {
		return routeRepository.findAll();
	}
	
	public Route findById(Integer id) throws RouteNotFoundException {
		Optional<Route> optionalRoute = routeRepository.findById(id);
		if(!optionalRoute.isPresent()) throw new RouteNotFoundException("No Route with ID: " + id + " exists.");
		return optionalRoute.get();
	}

	public List<Route> findBySearchAndFilter(HashMap<String, String> filterMap) {
		List<Route> routes = findAll();
		if(!filterMap.keySet().isEmpty()) routes = applyFilters(routes, filterMap);
		return routes;
	}

	public List<Route> applyFilters(List<Route> routes, HashMap<String, String> filterMap) {
				
		// Route ID
		String routeId = "routeId";
		if(filterMap.keySet().contains(routeId)) {
			try {
				Integer parsedRouteId = Integer.parseInt(filterMap.get(routeId));
				routes = routes.stream()
				.filter(i -> i.getRouteId().equals(parsedRouteId))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Origin IATA ID
		String routeOriginIataId = "routeOriginIataId";
		if(filterMap.keySet().contains(routeOriginIataId)) {
			String parsedRouteOriginIataId = filterMap.get(routeOriginIataId);
			routes = routes.stream()
			.filter(i -> i.getRouteOriginIataId().equals(parsedRouteOriginIataId))
			.collect(Collectors.toList());
		}

		// Destination IATA ID
		String routeDestinationIataId = "routeDestinationIataId";
		if(filterMap.keySet().contains(routeDestinationIataId)) {
			String parsedRouteDestinationIataId = filterMap.get(routeDestinationIataId);
			routes = routes.stream()
			.filter(i -> i.getRouteDestinationIataId().equals(parsedRouteDestinationIataId))
			.collect(Collectors.toList());
		}

		// Search - (applied last due to save CPU usage
		return applySearch(routes, filterMap);
	}

	public List<Route> applySearch(List<Route> routes, HashMap<String, String> filterMap) {
		List<Route> routesWithSearchTerms = new ArrayList<Route>();
		
		String searchTerms = "searchTerms";
		if(filterMap.keySet().contains(searchTerms)) {
			String formattedSearch = filterMap.get(searchTerms)
			.toLowerCase()
			.replace(", ", ",");
			String[] splitTerms = formattedSearch.split(",");
			ObjectMapper mapper = new ObjectMapper();
			
			for(Route route : routes) {
				boolean containsSearchTerms = true;
				
				try {
					String routeAsString = mapper.writeValueAsString(route)
					.toLowerCase()
					.replace("routeid", "")
					.replace("routeoriginiataid", "")
					.replace("routedestinationiataid", "");
					
					for(String term : splitTerms) {
						if(!routeAsString.contains(term)) {
							containsSearchTerms = false;
							break;
						}
					}
				} catch(JsonProcessingException err){
					containsSearchTerms = false;
				}

				if(containsSearchTerms) {
					routesWithSearchTerms.add(route);
				}
			}
		} else {
			return routes;
		}
		return routesWithSearchTerms;
	}

}