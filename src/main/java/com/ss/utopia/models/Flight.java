package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "flight")
public class Flight {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer flightId;

	@NotNull(message = "Route ID should not be empty")
	@ManyToOne
	@JoinColumn(name = "route_id")
	private Route flightRoute;

	@NotNull(message = "Airplane ID should not be empty")
	@ManyToOne
	@JoinColumn(name = "airplane_id")
	private @Valid Airplane flightAirplane;

	@NotNull(message = "Departure time should not be empty")
	@Column(name = "departure_time")
	private String flightDepartureTime;
	
	@NotNull(message = "Seating ID should not be empty")
	@GeneratedValue
	@Column(name = "seating_id")
	private Integer flightSeatingId;
	
	@NotNull(message="Duration should not be empty")
	@Column(name="duration")
	private Integer flightDuration;
	
	@NotNull(message="Status should not be empty")
	@Column(name="status")
	private String flightStatus;
	
	public Flight() {}
	public Flight(Integer flightId, 
		@NotNull(message = "Route ID should not be empty") Route flightRoute,
		@NotNull(message = "Airplane ID should not be empty") Airplane flightAirplane,
		@NotNull(message = "Departure time should not be empty") String flightDepartureTime,
		@NotNull(message = "Seating ID should not be empty") Integer flightSeatingId,
		@NotNull(message = "Duration should not be empty") Integer flightDuration,
		@NotNull(message = "Status should not be empty") String flightStatus) {

		this.flightId = flightId;
		this.flightRoute = flightRoute;
		this.flightAirplane = flightAirplane;
		this.flightDepartureTime = flightDepartureTime;
		this.flightSeatingId = flightSeatingId;
		this.flightDuration = flightDuration;
		this.flightStatus = flightStatus;
	}

	public Flight(Route flightRoute, Airplane flightAirplane, String flightDepartureTime, 
	Integer flightSeatingId, Integer flightDuration,String flightStatus) {
		this.flightRoute = flightRoute;
		this.flightAirplane = flightAirplane;
		this.flightDepartureTime = flightDepartureTime;
		this.flightSeatingId = flightSeatingId;
		this.flightDuration = flightDuration;
		this.flightStatus = flightStatus;
	}

	public Flight(
		@NotNull(message = "Route ID should not be empty") Route flightRoute,
		@NotNull(message = "Airplane ID should not be empty") Airplane flightAirplane,
		@NotNull(message = "Departure time should not be empty") String flightDepartureTime) {
		this.flightRoute = flightRoute;
		this.flightAirplane = flightAirplane;
		this.flightDepartureTime = flightDepartureTime;
	}

	public String getFlightDepartureTime() {
		return flightDepartureTime;
	}

	public void setFlightDepartureTime(String flightDepartureTime) {
		this.flightDepartureTime = flightDepartureTime;
	}

	public Integer getFlightSeatingId() {
		return flightSeatingId;
	}

	public void setFlightSeatingId(Integer flightSeatingId) {
		this.flightSeatingId = flightSeatingId;
	}

	public Integer getFlightDuration() {
		return flightDuration;
	}

	public void setFlightDuration(Integer flightDuration) {
		this.flightDuration = flightDuration;
	}

	public String getFlightStatus() {
		return flightStatus;
	}

	public void setFlightStatus(String flightStatus) {
		this.flightStatus = flightStatus;
	}

	public Integer getFlightId() {
		return flightId;
	}

	public void setFlightId(Integer flightId) {
		this.flightId = flightId;
	}

	public Route getFlightRoute() {
		return flightRoute;
	}

	public void setFlightRoute(Route flightRoute) {
		this.flightRoute = flightRoute;
	}

	public Airplane getFlightAirplane() {
		return flightAirplane;
	}

	public void setFlightAirplane(@Valid Airplane flightAirplane) {
		this.flightAirplane = flightAirplane;
	}
}