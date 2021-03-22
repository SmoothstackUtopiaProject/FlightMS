package com.ss.utopia.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ss.utopia.models.Airplane;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.Route;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {

	@Query(value="SELECT * FROM flight WHERE airplane_id = ?1", nativeQuery=true)
	List<Flight> findFlightsByAirplaneId(Integer airplaneId);

	@Query(value="SELECT * FROM route WHERE id = ?1", nativeQuery=true)
	Optional<Route> findRouteById(Integer routeId);

	@Query(value="SELECT * FROM airplane WHERE id = ?1", nativeQuery=true)
	Optional<Airplane> findAirplaneById(Integer airplaneId);

}
