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

  @Test
  void test_getAllFlights_returnsListOfFlights() {
    when(repository.findAll()).thenReturn(List.of(new Flight()));
    
    assertEquals(1, service.findAll().size());
  }

}

