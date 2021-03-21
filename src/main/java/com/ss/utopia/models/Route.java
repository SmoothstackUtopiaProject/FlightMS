package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "route")
public class Route {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer routeId;
	
	@ManyToOne
	@JoinColumn(name = "origin_Id")
	private Airport routeOrigin;

	@ManyToOne
	@JoinColumn(name = "destination_Id")
	private Airport routeDestination;

	public Route() {}
	public Route(Integer routeId, Airport routeOrigin, Airport routeDestination) {
		this.routeId = routeId;
		this.routeOrigin = routeOrigin;
		this.routeDestination = routeDestination;
	}
	public Route(Airport routeOrigin, Airport routeDestination) {
		this.routeOrigin = routeOrigin;
		this.routeDestination = routeDestination;
	}
	public Route(Integer routeId) {
		this.routeId = routeId;
	}
	
	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public Airport getRouteOrigin() {
		return routeOrigin;
	}

	public void setRouteOrigin(Airport routeOrigin) {
		this.routeOrigin = routeOrigin;
	}

	public Airport getRouteDestination() {
		return routeDestination;
	}

	public void setRouteDestination(Airport routeDestination) {
		this.routeDestination = routeDestination;
	}
	
}