package com.utopia.flight.controller;


import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utopia.flight.model.Flight;
import com.utopia.flight.service.FlightService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = FlightController.class)
@ActiveProfiles("test")
public class UserControllerTest {
	
    @Autowired                           
    private MockMvc mockMvc;  
	
	@MockBean
	private FlightService flightService;
	
    @Autowired
    private ObjectMapper objectMapper;
	
	List<Flight> flightList;
	
	private Flight flight;
	
	
	@BeforeEach
	void setUp() {
		LocalDate date1 = LocalDate.of(2021, 2, 18);
		LocalDate date2 = LocalDate.of(2021, 2, 19);
		LocalTime time1 = LocalTime.of(10, 30);
		LocalTime time2 = LocalTime.of(11, 30);
		flight = new Flight(100, 2, 5, date1, time1, 200.00, 200);
		Flight flight2 = new Flight(200, 2, 5, date2, time2, 200.00, 400);
		flightList = new ArrayList<>();
		flightList.add(flight);
		flightList.add(flight2);
		
	}
	
	
	@Test
	void testShouldFetchAllFlightsStatusOK() throws Exception{
		
		given(flightService.getAll()).willReturn(flightList);
		
		mockMvc.perform(get("/flights"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.size()", is(flightList.size())));
	}
	
	@Test
	void testShouldFetchAllFlightsStatusNoContent() throws Exception{
		List<Flight> flightList2 = new ArrayList<>();
		
		given(flightService.getAll()).willReturn(flightList2);
		mockMvc.perform(get("/flights"))
		.andExpect(status().isNoContent());
	}
	
	@Test
	void testShouldFetchOneUserByIdStatusOK() throws Exception{
		
		given(flightService.findById(flight.getId())).willReturn(Optional.of(flight));
		
		mockMvc.perform(get("/flights/id/{flightId}", flight.getId()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.routeId", is(flight.getRouteId())));
	}
	
	@Test
	void testShouldFetchOneUserByIdStatusNotFound() throws Exception{
		
		Integer flightId = 1;
		
		given(flightService.findById(flightId)).willReturn(Optional.empty());
		
		mockMvc.perform(get("/flights/id/{flightId}", flightId))
		.andExpect(status().isNotFound());
	}
	

	@Test
	void testShouldCreateNewFlightStatusCreated() throws JsonProcessingException, Exception {
		
		given(flightService.insert(any(Flight.class))).willAnswer((invocation) -> invocation.getArgument(0));

		Flight flightTest = new Flight(null, 2, 5, LocalDate.of(2021, 2, 18), LocalTime.of(10, 30), 200.00, null);
		
		mockMvc.perform(post("/flights")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flightTest)))
                .andExpect(status().isCreated());
	}
	

	@Test
	void testShouldCreateNewFlightStatusBadRequesRouteIdRequired() throws JsonProcessingException, Exception {
		
		
		Flight flightTest = new Flight(null, null, 5, LocalDate.of(2021, 2, 18), LocalTime.of(10, 30), 200.00, null);
		
		mockMvc.perform(post("/flights")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flightTest)))
                .andExpect(status().isBadRequest());
	}
	
	@Test
	void testShouldCreateNewFlightStatusBadRequesAirportIdRequired() throws JsonProcessingException, Exception {
		
		
		Flight flightTest = new Flight(null, 5, null, LocalDate.of(2021, 2, 18), LocalTime.of(10, 30), 200.00, null);
		
		mockMvc.perform(post("/flights")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flightTest)))
                .andExpect(status().isBadRequest());
	}
	
	@Test
	void testShouldCreateNewFlightStatusBadRequesDepartureDateRequired() throws JsonProcessingException, Exception {
		
		
		Flight flightTest = new Flight(null, 4, 5, null, LocalTime.of(10, 30), 200.00, null);
		
		mockMvc.perform(post("/flights")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flightTest)))
                .andExpect(status().isBadRequest());
	}
	
	@Test
	void testShouldCreateNewFlightStatusBadRequesDepartureTimeRequired() throws JsonProcessingException, Exception {
		
		
		Flight flightTest = new Flight(null, 4, 5, LocalDate.of(2021, 2, 18), null, 200.00, null);
		
		mockMvc.perform(post("/flights")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flightTest)))
                .andExpect(status().isBadRequest());
	}
	
	@Test
	void testShouldCreateNewFlightStatusBadRequesSeatPriceRequired() throws JsonProcessingException, Exception {
		
		
		Flight flightTest = new Flight(null, 4, 5, LocalDate.of(2021, 2, 18), LocalTime.of(10, 30), 0, null);
		
		mockMvc.perform(post("/flights")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flightTest)))
                .andExpect(status().isBadRequest());
	}
	
	
	
	@Test
	void shouldUpdateFlightStatusOk() throws JsonProcessingException, Exception {
		
		given(flightService.findById(flight.getId())).willReturn(Optional.of(flight));
        given(flightService.update(any(Flight.class))).willAnswer((invocation) -> invocation.getArgument(0));
        
        mockMvc.perform(put("/flights/id/{flightId}", flight.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isOk());
		
	}
		
	@Test
	void testShouldDeleteFlightStatusNoContent() throws Exception {
		
		given(flightService.findById(flight.getId())).willReturn(Optional.of(flight));
		
		mockMvc.perform(delete("/flights/id/{flightId}", flight.getId()))
        .andExpect(status().isOk());
		
	}
	
	@Test
	void testShouldDeleteFlightStatusNotFound() throws Exception {
		
		given(flightService.findById(0)).willReturn(Optional.empty());
		
		mockMvc.perform(delete("/flights/id/{flightId}", 0))
        .andExpect(status().isNotFound());
		
	}
	
//	@Test
//	void testShouldFindAllFlightsForGivenRouteAndDateStatusOk() throws Exception {
//		List<Flight> flightSearch = new ArrayList<>(); 
//		
//		String routeId = "4";
//		String date = "2021-02-18";
//		
//		Flight flightCase1 = new Flight(100, 4, 5, LocalDate.of(2021, 2, 18), LocalTime.of(10, 30), 0, null);
//		Flight flightCase2 = new Flight(200, 4, 5, LocalDate.of(2021, 2, 18), LocalTime.of(10, 30), 0, null);
//		flightSearch.add(flightCase1);
//		flightSearch.add(flightCase2);
//		
//		
//		given(flightService.search(routeId, date)).willReturn(flightSearch);
//		
//		mockMvc.perform(get("/flights/search/{routeId}/?date=2021-02-25", routeId))
//		.andExpect(status().isOk());
//	
//	}
	
	
	
	
	
	
	
	
	
	

	

	
	

}
