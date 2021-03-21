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
	private Airport routeOriginIataId;

	@ManyToOne
	@JoinColumn(name = "destination_Id")
	private Airport routeDestinationIataId;

	public Route() {}
	public Route(Integer routeId, Airport routeOriginIataId, Airport routeDestinationIataId) {
		this.routeId = routeId;
		this.routeOriginIataId = routeOriginIataId;
		this.routeDestinationIataId = routeDestinationIataId;
	}
	public Route(Airport routeOriginIataId, Airport routeDestinationIataId) {
		this.routeOriginIataId = routeOriginIataId;
		this.routeDestinationIataId = routeDestinationIataId;
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

	public Airport getRouteOriginIataId() {
		return routeOriginIataId;
	}

	public void setRouteOriginIataId(Airport routeOriginIataId) {
		this.routeOriginIataId = routeOriginIataId;
	}

	public Airport getRouteDestinationIataId() {
		return routeDestinationIataId;
	}

	public void setRouteDestinationIataId(Airport routeDestinationIataId) {
		this.routeDestinationIataId = routeDestinationIataId;
	}
	
}