package com.ss.utopia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exceptions.FlightNotFoundException;
import com.ss.utopia.exceptions.RouteNotFoundException;
import com.ss.utopia.models.Airplane;
import com.ss.utopia.models.AirplaneType;
import com.ss.utopia.models.Airport;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.Route;
import com.ss.utopia.services.FlightService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
class FlightControllerTest {

  private static final String SERVICE_PATH_FLIGHTS = "/flights";
  private final ObjectMapper mapper = new ObjectMapper();

  @InjectMocks
  private FlightController controller;

  @Mock
  private FlightService service;

  private MockMvc mvc;
  private HttpHeaders headers;

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
  void setup() throws Exception {
    mvc = MockMvcBuilders.standaloneSetup(controller).build();
    Mockito.reset(service);
    headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
  }

  // healthCheck
  //======================================================================= 
  @Test
  void test_healthCheck_thenStatus200() throws Exception {
    when(service.findAll()).thenReturn(List.of(mockFlight));

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_FLIGHTS + "/health")
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals("\"status\": \"up\"", response.getResponse().getContentAsString());
  }


  // findAll
  //=======================================================================
  @Test
  void test_findAllFlights_withValidFlights_thenStatus200() throws Exception {
    when(service.findAll()).thenReturn(List.of(mockFlight));

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_FLIGHTS)
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(List.of(mockFlight)), 
      response.getResponse().getContentAsString()
    );
  }

  @Test
  void test_findAllFlights_withNoValidFlights_thenStatus204() throws Exception {
    when(service.findAll()).thenReturn(Arrays.asList());

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_FLIGHTS)
      .headers(headers)
      )
      .andExpect(status().is(204))
      .andReturn();

    assertEquals("", response.getResponse().getContentAsString());
  }

  // findById
  //=======================================================================
  @Test
  void test_findById_withValidFlight_thenStatus200() throws Exception {
    when(service.findById(1)).thenReturn(mockFlight);

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_FLIGHTS + "/1")
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(mockFlight), 
      response.getResponse().getContentAsString()
    );
  }

  @Test
  void test_findById_withInvalidFlight_thenStatus404() throws Exception {
    when(service.findById(-1)).thenThrow(new FlightNotFoundException());

    mvc
      .perform(get(SERVICE_PATH_FLIGHTS + "/-1")
      .headers(headers)
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  @Test
  void test_findById_withBadParams_thenStatus400() throws Exception {
    mvc
      .perform(get(SERVICE_PATH_FLIGHTS + "/xxx")
      .headers(headers)
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  // findBySearchAndFilter
  //=======================================================================
  @Test
  void test_findBySearchAndFilter_withValidFlights_thenStatus200() throws Exception {
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("searchTerms", "");

    when(service.findBySearchAndFilter(filterMap)).thenReturn(List.of(mockFlight));

    MvcResult response = mvc
      .perform(post(SERVICE_PATH_FLIGHTS + "/search")
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(List.of(mockFlight)),
      response.getResponse().getContentAsString()
    );
  }

  @Test
  void test_findBySearchAndFilter_withNoValidFlights_thenStatus204() throws Exception {
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("searchTerms", "XXXxx");

    when(service.findBySearchAndFilter(filterMap))
    .thenReturn(Arrays.asList());

    MvcResult response = mvc
      .perform(post(SERVICE_PATH_FLIGHTS + "/search")
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(204))
      .andReturn();

    assertEquals("", response.getResponse().getContentAsString());
  }

  @Test
  void test_findBySearchAndFilter_withInvalidParams_thenStatus400() throws Exception {
    mvc
      .perform(post(SERVICE_PATH_FLIGHTS + "/search")
      .headers(headers)
      .content("NotAJSONObject")
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  // insert
  //=======================================================================
  @Test
  void test_insert_withValidFlight_thenStatus201() throws Exception {
    when(service.insert(
        mockRouteId, mockAirplaneId, 
        mockDeparture, mockSeatingId, 
        mockDuration, mockStatus)).thenReturn(mockFlight);

    MvcResult response = mvc
      .perform(post(SERVICE_PATH_FLIGHTS)
      .headers(headers)
      .content(mapper.writeValueAsString(mockFlight))
      )
      .andExpect(status().is(201))
      .andReturn();

    assertEquals(mapper.writeValueAsString(mockFlight), response.getResponse().getContentAsString());
  }

  @Test
  void test_insert_withNonExistingRoute_thenStatus404() throws Exception {
    Flight invalidFlight = mockFlight;
    invalidFlight.setFlightRoute(new Route(-1));

    when(service.insert(
        -1, 
        invalidFlight.getFlightAirplane().getAirplaneId(), 
        invalidFlight.getFlightDepartureTime(), 
        invalidFlight.getFlightSeatingId(), 
        invalidFlight.getFlightDuration(), 
        invalidFlight.getFlightStatus())).thenThrow(new RouteNotFoundException());

    mvc
      .perform(post(SERVICE_PATH_FLIGHTS)
      .headers(headers)
      .content(mapper.writeValueAsString(invalidFlight))
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  // update
  //=======================================================================
  @Test
  void test_update_withValidFlight_thenStatus202() throws Exception {

    when(service.update(mockFlightId,
        mockRouteId, mockAirplaneId, 
        mockDeparture, mockSeatingId, 
        mockDuration, mockStatus)).thenReturn(mockFlight);

    MvcResult response = mvc
      .perform(put(SERVICE_PATH_FLIGHTS)
      .headers(headers)
      .content(mapper.writeValueAsString(mockFlight))
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(mockFlight), response.getResponse().getContentAsString());
  }

  @Test
  void test_update_withNonExistingFlight_thenStatus404() throws Exception {
    Flight invalidFlight = mockFlight;
    invalidFlight.setFlightId(-1);

    when(service.update(invalidFlight.getFlightId(),
        mockRouteId, mockAirplaneId, 
        mockDeparture, mockSeatingId, 
        mockDuration, mockStatus)).thenThrow(new FlightNotFoundException());

    mvc
      .perform(put(SERVICE_PATH_FLIGHTS)
      .headers(headers)
      .content(mapper.writeValueAsString(invalidFlight))
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  @Test
  void test_update_withNonExistingRoute_thenStatus404() throws Exception {
    Flight invalidFlight = mockFlight;
    invalidFlight.setFlightRoute(new Route(-1));

    when(service.update(mockFlightId,
        invalidFlight.getFlightRoute().getRouteId(), mockAirplaneId, 
        mockDeparture, mockSeatingId, 
        mockDuration, mockStatus)).thenThrow(new RouteNotFoundException());

    mvc
      .perform(put(SERVICE_PATH_FLIGHTS)
      .headers(headers)
      .content(mapper.writeValueAsString(invalidFlight))
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  // delete
  //=======================================================================
  @Test
  void test_delete_withValidFlight_thenStatus202() throws Exception {

    mvc
      .perform(delete(SERVICE_PATH_FLIGHTS + "/1")
      .headers(headers)
      )
      .andExpect(status().is(202))
      .andReturn();
  }

  @Test
  void test_delete_withNonExistingFlight_thenStatus404() throws Exception {
    
    when(service.deleteById(-1)).thenThrow(new FlightNotFoundException());

    mvc
      .perform(delete(SERVICE_PATH_FLIGHTS + "/-1")
      .headers(headers)
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  @Test
  void test_delete_withInvalidParams_thenStatus400() throws Exception {
    mvc
      .perform(delete(SERVICE_PATH_FLIGHTS + "/NOT_AN_INTEGER")
      .headers(headers)
      )
      .andExpect(status().is(400))
      .andReturn();
  }
}
