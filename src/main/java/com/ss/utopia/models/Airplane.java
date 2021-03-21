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
	private AirplaneType airplaneType;

	public Airplane() {}
	public Airplane(AirplaneType airplaneType) {
		this.airplaneType = airplaneType;
	}
	public Airplane(Integer airplaneId, AirplaneType airplaneType) {
		this.airplaneId = airplaneId;
		this.airplaneType = airplaneType;
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

	public AirplaneType getAirplaneType() {
		return airplaneType;
	}

	public void setAirplaneType(AirplaneType airplaneType) {
		this.airplaneType = airplaneType;
	}
	
}