package com.apap.tutorial6.service;

import java.util.Optional;
import java.util.List;
import com.apap.tutorial6.model.FlightModel;

/**
 * FlightService
 */
public interface FlightService {
    FlightModel addFlight(FlightModel flight);
    
    void deleteByFlightNumber(String flightNumber);

    Optional<FlightModel> getFlightDetailByFlightNumber(String flightNumber);
    
    Optional<FlightModel> getFlightDetailById(long id);
    
    List<FlightModel> getAllFlights();
}