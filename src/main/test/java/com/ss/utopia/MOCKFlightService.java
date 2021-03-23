package com.ss.utopia;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ss.utopia.models.Airplane;
import com.ss.utopia.models.AirplaneType;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.Route;

public class MOCKFlightService {

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

  private static final Flight[] testFlightArray = {
    testFlight,
    new Flight(2, "ACTIVE", "bhdjkHKKKAJS"),
    new Flight(3, "ONHOLD", "UOIUHKJAHSAS"),
    new Flight(4, "INACTIVE", "WYTWHJKASFHJ"),
    new Flight(5, "ACTIVE", "PIPOIMBJJSSJ"),
    new Flight(6, "INACTIVE", "RTYCGZNCBCCC"),
    new Flight(7, "ACTIVE", "MOKJOIASJKHD"),
    new Flight(8, "INACTIVE", "UIASHASJKKZC"),
    new Flight(9, "ACTIVE", "QQWASNDAJSDK"),
  };

  public Flight(Integer flightId, 
  @NotNull(message = "Route ID should not be empty") Route flightRoute,
  @NotNull(message = "Airplane ID should not be empty") Airplane flightAirplane,
  @NotNull(message = "Departure time should not be empty") String flightDepartureTime,
  @NotNull(message = "Seating ID should not be empty") Integer flightSeatingId,
  @NotNull(message = "Duration should not be empty") Integer flightDuration,
  @NotNull(message = "Status should not be empty") String flightStatus) {


  public static Flight getTestFlight() {
    return testFlight;
  }

  public static List<Flight> getTestFlightList() {
    return Arrays.asList(testFlightArray);
  }

  public static List<Flight> findAllWithResults() {
    return getTestFlightList();
  }

  public static List<Flight> findAllWithNoResults() {
    List<Flight> emptyFlightList = Arrays.asList();
    return emptyFlightList;
  }

  public static FlightWithReferenceData findById(Integer id) {
    List<FlightWithReferenceData> flightByIdList = getTestFlightWithReferenceDataList().stream()
      .filter(i -> i.getFlightId().equals(id))
      .collect(Collectors.toList());
    return !flightByIdList.isEmpty()
      ? flightByIdList.get(0)
      : null;
  }

  public static Flight save(Flight flight) {
    return flight;
  }

  public static String deleteById(Integer id) {
    return "Flight with ID: " + id + " was deleted.";
  }
}