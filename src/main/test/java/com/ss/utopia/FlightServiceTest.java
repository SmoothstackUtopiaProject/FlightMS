package com.ss.utopia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ss.utopia.repositories.FlightRepository;
import com.ss.utopia.services.FlightService;
import com.ss.utopia.exceptions.FlightAlreadyExistsException;
import com.ss.utopia.exceptions.FlightNotFoundException;
import com.ss.utopia.models.Flight;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class FlightServiceTest {

  @MockBean
  private FlightRepository repository;

  @Autowired
  private FlightService service;

  private static final Flight testFlight = new Flight(
    1, 
    new Route(
      1, 
      new Airport(
        "DCA", 
        "Ronald Reagan Inetnational Airport", 
        "Washington D.C."
      ), 
      new Airport(
        "DCA", 
        "Ronald Reagan Inetnational Airport", 
        "Washington D.C."
      )
    ),
    new Airplane(
      1,
      new AirplaneType(
        1, 
        "Airbus A350",
        304,
        10,
        24,
        270,
        2,
        4,
        6,
        "7-22-45"
      )
    ),
    "2020-4-10 07:43:00",
    1,
    43000,
    "ACTIVE"
  );

  @Test
  void test_getAllFlights_returnsListOfFlights() {
    when(repository.findAll()).thenReturn(List.of(new Flight()));
    
    assertEquals(1, service.findAll().size());
  }

}

