package com.ss.utopia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.ss.utopia.repositories.FlightRepository;
import com.ss.utopia.services.FlightService;
import com.ss.utopia.exceptions.AirplaneNotFoundException;
import com.ss.utopia.exceptions.FlightAlreadyExistsException;
import com.ss.utopia.exceptions.FlightNotFoundException;
import com.ss.utopia.exceptions.RouteNotFoundException;
import com.ss.utopia.filters.FlightFilters;
import com.ss.utopia.models.AirplaneType;
import com.ss.utopia.models.Airplane;
import com.ss.utopia.models.Airport;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.Route;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FlightServiceTest {

  @Mock
  private FlightRepository repository;

  @InjectMocks
  private FlightService service;

  private static final Flight mockFlight = new Flight(
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

  final Integer mockFlightId = mockFlight.getFlightId();
  final Integer mockRouteId = mockFlight.getFlightRoute().getRouteId();
  final Integer mockAirplaneId = mockFlight.getFlightAirplane().getAirplaneId();
  final String mockDeparture = mockFlight.getFlightDepartureTime();
  final Integer mockSeatingId = mockFlight.getFlightSeatingId();
  final Integer mockDuration = mockFlight.getFlightDuration();
  final String mockStatus = mockFlight.getFlightStatus();
  final String storedDeparture = "2020-4-11 07:43:00";

  @BeforeEach
  void beforeEach() {
    when(repository.findById(mockFlightId)).thenReturn(Optional.of(mockFlight));
    when(repository.findRouteById(mockRouteId)).thenReturn(Optional.of(mockFlight.getFlightRoute()));
    when(repository.findFlightsByAirplaneId(mockAirplaneId)).thenReturn(List.of(mockFlight));
    when(repository.findAirplaneById(mockAirplaneId)).thenReturn(Optional.of(mockFlight.getFlightAirplane()));
  }

  @Test
  void test_getAllFlights_returnsListOfFlights() {
    when(repository.findAll()).thenReturn(List.of(new Flight()));
    
    assertEquals(1, service.findAll().size());
  }

  @Test
  void test_findById_returnsAFlight() throws Exception{
    when(repository.findById(mockFlight.getFlightId())).thenReturn(Optional.of(mockFlight));

    assertEquals(mockFlight, service.findById(mockFlight.getFlightId()));
  }

  @Test
  void test_findById_throwsFlightNotFoundException() {
    when(repository.findById(mockFlight.getFlightId())).thenReturn(Optional.empty());
    
    assertThrows(FlightNotFoundException.class, () -> service.findById(mockFlight.getFlightId()));
  }

  @Test
  void test_insert_returnsCreatedFlight() throws Exception{
    when(repository.save(mockFlight)).thenReturn(mockFlight);
    Flight returnedFlight = service.insert(mockRouteId, mockAirplaneId, storedDeparture, mockSeatingId, mockDuration, mockStatus);

    assertEquals(mockRouteId, returnedFlight.getFlightRoute().getRouteId());
  }

  @Test
  void test_insert_throwsRouteNotFoundException() throws Exception {
    when(repository.findRouteById(mockRouteId)).thenReturn(Optional.empty());

    assertThrows(RouteNotFoundException.class, () -> service.insert(mockRouteId, mockAirplaneId, mockDeparture, mockSeatingId, mockDuration, mockStatus));
  }

  @Test
  void test_insert_throwsAirplaneNotFoundException() throws Exception {
    when(repository.findAirplaneById(mockAirplaneId)).thenReturn(Optional.empty());

    assertThrows(AirplaneNotFoundException.class, () -> service.insert(mockRouteId, mockAirplaneId, mockDeparture, mockSeatingId, mockDuration, mockStatus));
  }

  @Test
  void test_update_throwsFlightNotFoundException() throws Exception {
    when(repository.findById(mockFlightId)).thenReturn(Optional.empty());

    assertThrows(FlightNotFoundException.class, () -> service.update(mockFlightId, mockRouteId, mockAirplaneId, mockDeparture, mockSeatingId, mockDuration, mockStatus));
  }

  @Test
  void test_update_throwsRouteNotFoundException() throws Exception {
    when(repository.findRouteById(mockRouteId)).thenReturn(Optional.empty());

    assertThrows(RouteNotFoundException.class, () -> service.update(mockFlightId, mockRouteId, mockAirplaneId, mockDeparture, mockSeatingId, mockDuration, mockStatus));
  }

  @Test
  void test_update_throwsAirplaneNotFoundException() throws Exception {
    when(repository.findAirplaneById(mockAirplaneId)).thenReturn(Optional.empty());

    assertThrows(AirplaneNotFoundException.class, () -> service.update(mockFlightId, mockRouteId, mockAirplaneId, mockDeparture, mockSeatingId, mockDuration, mockStatus));
  }

  @Test
  void test_delete_throwsFlightNotFoundException() {
    when(repository.findById(mockFlightId)).thenReturn(Optional.empty());

    assertThrows(FlightNotFoundException.class, () -> service.deleteById(mockFlightId));
  }

  @Test
  void test_delete_success() throws Exception {
    assertEquals(("Flight with ID: " + mockFlightId + " was deleted."), service.deleteById(mockFlightId));
  }

  @Test
  void test_searchAndFilter_withResults() throws Exception {
    String searchTerm = "Washington";

    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("searchTerms", searchTerm);

    List<Flight> searchAndFilterResults = FlightFilters.apply(List.of(mockFlight), filterMap);
    assertEquals(1, searchAndFilterResults.size());
  }

  @Test
  void test_searchAndFilter_withNoResults() throws Exception {
    String searchTerm = "XXXX";
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("searchTerms", searchTerm);

    List<Flight> searchAndFilterResults = FlightFilters.apply(List.of(mockFlight), filterMap);
    assertEquals(0, searchAndFilterResults.size());
  }
}

