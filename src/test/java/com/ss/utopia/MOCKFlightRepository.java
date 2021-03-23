// package com.ss.utopia;

// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;
// import java.util.stream.Collectors;

// import com.ss.utopia.models.Flight;

// public class MOCKFlightRepository {

//   private static final Flight testFlight = new Flight(1, "ACTIVE", "AFHAJKFHKAJS");

//   private static final Flight[] testFlightArray = {
//     testFlight,
//     new Flight(2, "ACTIVE", "bhdjkHKKKAJS"),
//     new Flight(3, "ONHOLD", "UOIUHKJAHSAS"),
//     new Flight(4, "INACTIVE", "WYTWHJKASFHJ"),
//     new Flight(5, "ACTIVE", "PIPOIMBJJSSJ"),
//     new Flight(6, "INACTIVE", "RTYCGZNCBCCC"),
//     new Flight(7, "ACTIVE", "MOKJOIASJKHD"),
//     new Flight(8, "INACTIVE", "UIASHASJKKZC"),
//     new Flight(9, "ACTIVE", "QQWASNDAJSDK"),
//   };


//   public static Flight getTestFlight() {
//     return testFlight;
//   }

//   public static List<Flight> getTestFlightList() {
//     return Arrays.asList(testFlightArray);
//   }

//   public static List<Flight> findAllWithResults() {
//     return getTestFlightList();
//   }

//   public static List<Flight> findAllWithNoResults() {
//     List<Flight> emptyFlightList = Arrays.asList();
//     return emptyFlightList;
//   }

//   public static Optional<Flight> findById(Integer id) {
//     List<Flight> bookingByIdList = getTestFlightList().stream()
//       .filter(i -> i.getFlightId().equals(id))
//       .collect(Collectors.toList());
//     return Optional.of(bookingByIdList.get(0));
//   }

//   public static Flight save(Flight booking) {
//     return booking;
//   }
// }