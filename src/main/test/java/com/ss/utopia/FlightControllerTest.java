// package com.ss.utopia;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.util.Map;
// import java.util.Arrays;
// import java.util.HashMap;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.ss.utopia.exceptions.FlightNotFoundException;
// import com.ss.utopia.exceptions.FlightUserNotFoundException;
// import com.ss.utopia.models.FlightWithReferenceData;
// import com.ss.utopia.services.FlightService;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.MvcResult;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// @SpringBootTest
// class FlightControllerTest {

//   private static final String SERVICE_PATH_FLIGHTS = "/flights";
//   private final ObjectMapper mapper = new ObjectMapper();

//   @InjectMocks
//   private FlightController controller;

//   @Mock
//   private FlightService service;

//   private MockMvc mvc;
//   private HttpHeaders headers;

//   @BeforeEach
//   void setup() throws Exception {
//     mvc = MockMvcBuilders.standaloneSetup(controller).build();
//     Mockito.reset(service);
//     headers = new HttpHeaders();
//     headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//     headers.setContentType(MediaType.APPLICATION_JSON);
//   }


//   // validateModel
//   //=======================================================================
//   @Test
//   void test_validFlightTestModel() throws Exception {
//     assertEquals(Integer.valueOf(1), MOCKFlightService.getTestFlight().getFlightId());
//     assertEquals("ACTIVE", MOCKFlightService.getTestFlight().getFlightStatus());
//     assertEquals("AFHAJKFHKAJS", MOCKFlightService.getTestFlight().getFlightConfirmationCode());
//   }


//   // healthCheck
//   //======================================================================= 
//   @Test
//   void test_healthCheck_thenStatus200() throws Exception {
//     when(service.findAll()).thenReturn(MOCKFlightService.findAllWithResults());

//     MvcResult response = mvc
//       .perform(get(SERVICE_PATH_FLIGHTS + "/health")
//       .headers(headers)
//       )
//       .andExpect(status().is(200))
//       .andReturn();

//     assertEquals("\"status\": \"up\"", response.getResponse().getContentAsString());
//   }


//   // findAll
//   //=======================================================================
//   @Test
//   void test_findAllFlights_withValidFlights_thenStatus200() throws Exception {
//     when(service.findAll()).thenReturn(MOCKFlightService.findAllWithResults());

//     MvcResult response = mvc
//       .perform(get(SERVICE_PATH_FLIGHTS)
//       .headers(headers)
//       )
//       .andExpect(status().is(200))
//       .andReturn();

//     assertEquals(mapper.writeValueAsString(
//       MOCKFlightService.getTestFlightList()), 
//       response.getResponse().getContentAsString()
//     );
//   }

//   @Test
//   void test_findAllFlights_withNoValidFlights_thenStatus204() throws Exception {
//     when(service.findAll()).thenReturn(MOCKFlightService.findAllWithNoResults());

//     MvcResult response = mvc
//       .perform(get(SERVICE_PATH_FLIGHTS)
//       .headers(headers)
//       )
//       .andExpect(status().is(204))
//       .andReturn();

//     assertEquals("", response.getResponse().getContentAsString());
//   }

//   // findAllWithReferenceData
//   //=======================================================================
//   @Test
//   void test_findAllFlightsWithReferenceData_withValidFlights_thenStatus200() throws Exception {
//     when(service.findAllWithReferenceData()).thenReturn(MOCKFlightService.findAllWithReferenceDataWithResults());

//     MvcResult response = mvc
//       .perform(get(SERVICE_PATH_FLIGHTS + "/referencedata")
//       .headers(headers)
//       )
//       .andExpect(status().is(200))
//       .andReturn();

//     assertEquals(mapper.writeValueAsString(
//       MOCKFlightService.getTestFlightWithReferenceDataList()),
//       response.getResponse().getContentAsString()
//     );
//   }

//   @Test
//   void test_findAllFlightsWithReferenceData_withNoValidFlights_thenStatus204() throws Exception {
//     when(service.findAllWithReferenceData()).thenReturn(MOCKFlightService.findAllWithReferenceDataWithNoResults());

//     MvcResult response = mvc
//       .perform(get(SERVICE_PATH_FLIGHTS + "/referencedata")
//       .headers(headers)
//       )
//       .andExpect(status().is(204))
//       .andReturn();

//     assertEquals("", response.getResponse().getContentAsString());
//   }


//   // findById
//   //=======================================================================
//   @Test
//   void test_findById_withValidFlight_thenStatus200() throws Exception {
//     when(service.findByIdWithReferenceData(1)).thenReturn(MOCKFlightService.findById(1));

//     MvcResult response = mvc
//       .perform(get(SERVICE_PATH_FLIGHTS + "/1")
//       .headers(headers)
//       )
//       .andExpect(status().is(200))
//       .andReturn();

//     assertEquals(mapper.writeValueAsString(
//       MOCKFlightService.getTestFlightWithReferenceData()), 
//       response.getResponse().getContentAsString()
//     );
//   }

//   @Test
//   void test_findById_withInvalidFlight_thenStatus404() throws Exception {
//     when(service.findByIdWithReferenceData(-1)).thenThrow(new FlightNotFoundException());

//     mvc
//       .perform(get(SERVICE_PATH_FLIGHTS + "/-1")
//       .headers(headers)
//       )
//       .andExpect(status().is(404))
//       .andReturn();
//   }

//   @Test
//   void test_findById_withBadParams_thenStatus400() throws Exception {
//     mvc
//       .perform(get(SERVICE_PATH_FLIGHTS + "/NotAnInteger")
//       .headers(headers)
//       )
//       .andExpect(status().is(400))
//       .andReturn();
//   }

//   // findBySearchAndFilter
//   //=======================================================================
//   @Test
//   void test_findBySearchAndFilter_withValidFlights_thenStatus200() throws Exception {
    
//     // An empty filterMap through findBySearchAndFilter should return all
//     Map<String, String> filterMap = new HashMap<>();
//     filterMap.put("searchTerms", "");

//     when(service.findBySearchAndFilter(filterMap)).thenReturn(MOCKFlightService.findAllWithReferenceDataWithResults());

//     MvcResult response = mvc
//       .perform(post(SERVICE_PATH_FLIGHTS + "/search")
//       .headers(headers)
//       .content(mapper.writeValueAsString(filterMap))
//       )
//       .andExpect(status().is(200))
//       .andReturn();

//     assertEquals(mapper.writeValueAsString(
//       MOCKFlightService.getTestFlightWithReferenceDataList()),
//       response.getResponse().getContentAsString()
//     );
//   }

//   @Test
//   void test_findBySearchAndFilter_withNoValidFlights_thenStatus204() throws Exception {
    
//     // A bookingId filter of "-1" through findBySearchAndFilter should return empty
//     Map<String, String> filterMap = new HashMap<>();
//     filterMap.put("bookingId", "-1");

//     when(service.findBySearchAndFilter(filterMap))
//     .thenReturn(MOCKFlightService.findAllWithReferenceDataWithNoResults());

//     MvcResult response = mvc
//       .perform(post(SERVICE_PATH_FLIGHTS + "/search")
//       .headers(headers)
//       .content(mapper.writeValueAsString(filterMap))
//       )
//       .andExpect(status().is(204))
//       .andReturn();

//     assertEquals("", response.getResponse().getContentAsString());
//   }

//   @Test
//   void test_findBySearchAndFilter_withInvalidParams_thenStatus400() throws Exception {
//     mvc
//       .perform(post(SERVICE_PATH_FLIGHTS + "/search")
//       .headers(headers)
//       .content("NotAJSONObject")
//       )
//       .andExpect(status().is(400))
//       .andReturn();
//   }

//   // insert
//   //=======================================================================
//   @Test
//   void test_insert_withValidFlight_thenStatus201() throws Exception {
    
//     FlightWithReferenceData testFlight = MOCKFlightService.getTestFlightWithReferenceData();
//     Map<String, String> filterMap = new HashMap<>();
//     filterMap.put("bookingUserId", testFlight.getFlightUserId().toString());
//     filterMap.put("bookingFlightId", testFlight.getFlightFlightId().toString());
//     filterMap.put("bookingGuestEmail", testFlight.getFlightGuestEmail());
//     filterMap.put("bookingGuestPhone", testFlight.getFlightGuestPhone());

//     when(service.insert(filterMap)).thenReturn(testFlight);

//     MvcResult response = mvc
//       .perform(post(SERVICE_PATH_FLIGHTS)
//       .headers(headers)
//       .content(mapper.writeValueAsString(filterMap))
//       )
//       .andExpect(status().is(201))
//       .andReturn();

//     assertEquals(mapper.writeValueAsString(testFlight), response.getResponse().getContentAsString());
//   }

//   @Test
//   void test_insert_withNonExistingFlightUser_thenStatus404() throws Exception {
    
//     FlightWithReferenceData testFlight = MOCKFlightService.getTestFlightWithReferenceData();
//     Map<String, String> filterMap = new HashMap<>();
//     filterMap.put("bookingUserId", testFlight.getFlightUserId().toString());
//     filterMap.put("bookingFlightId", testFlight.getFlightFlightId().toString());
//     filterMap.put("bookingGuestEmail", testFlight.getFlightGuestEmail());
//     filterMap.put("bookingGuestPhone", testFlight.getFlightGuestPhone());

//     when(service.insert(filterMap)).thenThrow(new FlightUserNotFoundException());

//     mvc
//       .perform(post(SERVICE_PATH_FLIGHTS)
//       .headers(headers)
//       .content(mapper.writeValueAsString(filterMap))
//       )
//       .andExpect(status().is(404))
//       .andReturn();
//   }

//   @Test
//   void test_insert_withInvalidParams_thenStatus400() throws Exception {
    
//     Map<String, String> filterMap = new HashMap<>();
//     filterMap.put("bookingUserId", "NOT_A_VALID_USER_ID");
//     filterMap.put("bookingFlightId", "NOT_A_VALID_FLIGHT_ID");
//     filterMap.put("bookingGuestEmail", "NOT_A_VALID_GUEST_EMAIL");
//     filterMap.put("bookingGuestPhone", "NOT_A_VALID_GUEST_PHONE");

//     when(service.insert(filterMap)).thenThrow(new NumberFormatException());

//     mvc
//       .perform(post(SERVICE_PATH_FLIGHTS)
//       .headers(headers)
//       .content(mapper.writeValueAsString(filterMap))
//       )
//       .andExpect(status().is(400))
//       .andReturn();
//   }

//   // update
//   //=======================================================================
//   @Test
//   void test_update_withValidFlight_thenStatus202() throws Exception {
    
//     FlightWithReferenceData testFlight = MOCKFlightService.getTestFlightWithReferenceData();
//     Map<String, String> filterMap = new HashMap<>();
//     filterMap.put("bookingId", testFlight.getFlightId().toString());
//     filterMap.put("bookingStatus", testFlight.getFlightStatus());
//     filterMap.put("bookingUserId", testFlight.getFlightUserId().toString());
//     filterMap.put("bookingFlightId", testFlight.getFlightFlightId().toString());
//     filterMap.put("bookingGuestEmail", testFlight.getFlightGuestEmail());
//     filterMap.put("bookingGuestPhone", testFlight.getFlightGuestPhone());

//     when(service.update(filterMap)).thenReturn(testFlight);

//     MvcResult response = mvc
//       .perform(put(SERVICE_PATH_FLIGHTS)
//       .headers(headers)
//       .content(mapper.writeValueAsString(filterMap))
//       )
//       .andExpect(status().is(202))
//       .andReturn();

//     assertEquals(mapper.writeValueAsString(testFlight), response.getResponse().getContentAsString());
//   }

//   @Test
//   void test_update_withNonExistingFlight_thenStatus404() throws Exception {
    
//     FlightWithReferenceData testFlight = MOCKFlightService.getTestFlightWithReferenceData();
//     Map<String, String> filterMap = new HashMap<>();
//     filterMap.put("bookingId", "-1");
//     filterMap.put("bookingStatus", testFlight.getFlightStatus());
//     filterMap.put("bookingUserId", testFlight.getFlightUserId().toString());
//     filterMap.put("bookingFlightId", testFlight.getFlightFlightId().toString());
//     filterMap.put("bookingGuestEmail", testFlight.getFlightGuestEmail());
//     filterMap.put("bookingGuestPhone", testFlight.getFlightGuestPhone());

//     when(service.update(filterMap)).thenThrow(new FlightNotFoundException());

//     mvc
//       .perform(put(SERVICE_PATH_FLIGHTS)
//       .headers(headers)
//       .content(mapper.writeValueAsString(filterMap))
//       )
//       .andExpect(status().is(404))
//       .andReturn();
//   }

//   @Test
//   void test_update_withNonExistingFlightUser_thenStatus404() throws Exception {
    
//     FlightWithReferenceData testFlight = MOCKFlightService.getTestFlightWithReferenceData();
//     Map<String, String> filterMap = new HashMap<>();
//     filterMap.put("bookingId", testFlight.getFlightId().toString());
//     filterMap.put("bookingStatus", testFlight.getFlightStatus());
//     filterMap.put("bookingUserId", "-1");
//     filterMap.put("bookingFlightId", testFlight.getFlightFlightId().toString());
//     filterMap.put("bookingGuestEmail", testFlight.getFlightGuestEmail());
//     filterMap.put("bookingGuestPhone", testFlight.getFlightGuestPhone());

//     when(service.update(filterMap)).thenThrow(new FlightUserNotFoundException());

//     mvc
//       .perform(put(SERVICE_PATH_FLIGHTS)
//       .headers(headers)
//       .content(mapper.writeValueAsString(filterMap))
//       )
//       .andExpect(status().is(404))
//       .andReturn();
//   }

//   @Test
//   void test_update_withInvalidParams_thenStatus400() throws Exception {
    
//     FlightWithReferenceData testFlight = MOCKFlightService.getTestFlightWithReferenceData();
//     Map<String, String> filterMap = new HashMap<>();
//     filterMap.put("bookingId", "NOT_A_VALID_BOOKING_ID");
//     filterMap.put("bookingStatus", testFlight.getFlightStatus());
//     filterMap.put("bookingUserId", testFlight.getFlightUserId().toString());
//     filterMap.put("bookingFlightId", testFlight.getFlightFlightId().toString());
//     filterMap.put("bookingGuestEmail", testFlight.getFlightGuestEmail());
//     filterMap.put("bookingGuestPhone", testFlight.getFlightGuestPhone());

//     when(service.update(filterMap)).thenThrow(new NumberFormatException());

//     mvc
//       .perform(put(SERVICE_PATH_FLIGHTS)
//       .headers(headers)
//       .content(mapper.writeValueAsString(filterMap))
//       )
//       .andExpect(status().is(400))
//       .andReturn();
//   }

//   // delete
//   //=======================================================================
//   @Test
//   void test_delete_withValidFlight_thenStatus202() throws Exception {

//     mvc
//       .perform(delete(SERVICE_PATH_FLIGHTS + "/1")
//       .headers(headers)
//       )
//       .andExpect(status().is(202))
//       .andReturn();
//   }

//   @Test
//   void test_delete_withNonExistingFlight_thenStatus404() throws Exception {
    
//     when(service.delete(-1)).thenThrow(new FlightNotFoundException());

//     mvc
//       .perform(delete(SERVICE_PATH_FLIGHTS + "/-1")
//       .headers(headers)
//       )
//       .andExpect(status().is(404))
//       .andReturn();
//   }

//   @Test
//   void test_delete_withInvalidParams_thenStatus400() throws Exception {
//     mvc
//       .perform(delete(SERVICE_PATH_FLIGHTS + "/NOT_AN_INTEGER")
//       .headers(headers)
//       )
//       .andExpect(status().is(400))
//       .andReturn();
//   }
// }
