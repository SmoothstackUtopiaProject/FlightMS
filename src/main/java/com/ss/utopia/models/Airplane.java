package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "airplane")
public class Airplane {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer airplaneId;
	
	@NotNull(message = "Type ID should not be empty")
	@ManyToOne
	@JoinColumn(name = "type_id", nullable = false)
	private AirplaneType airplaneTypeId;

	public Airplane() {}
	public Airplane(AirplaneType airplaneTypeId) {
		this.airplaneTypeId = airplaneTypeId;
	}
	public Airplane(Integer airplaneId, AirplaneType airplaneTypeId) {
		this.airplaneId = airplaneId;
		this.airplaneTypeId = airplaneTypeId;
	}
	public Airplane(Integer airplaneId) {
		this.airplaneId = airplaneId;
	}

	public Integer getAirplaneId() {
		return airplaneId;
	}

	public void setAirplaneId(Integer airplaneId) {
		this.airplaneId = airplaneId;
	}

	public AirplaneType getAirplaneTypeId() {
		return airplaneTypeId;
	}

	public void setAirplaneTypeId(AirplaneType airplaneTypeId) {
		this.airplaneTypeId = airplaneTypeId;
	}
	
}