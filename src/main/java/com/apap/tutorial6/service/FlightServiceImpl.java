package com.apap.tutorial6.service;

import java.util.Optional;
import java.util.List;

import com.apap.tutorial6.model.FlightModel;
import com.apap.tutorial6.repository.FlightDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FlightServiceImpl
 */
@Service
@Transactional
public class FlightServiceImpl implements FlightService {
    @Autowired
    private FlightDb flightDb;
    
    @Override
    public FlightModel addFlight(FlightModel flight) {
        return flightDb.save(flight);
    }

    @Override
    public void deleteByFlightNumber(String flightNumber) {
        flightDb.deleteByFlightNumber(flightNumber);
    }

    @Override
    public Optional<FlightModel> getFlightDetailByFlightNumber(String flightNumber) {
        return flightDb.findByFlightNumber(flightNumber);
    }
    
    @Override
    public Optional<FlightModel> getFlightDetailById(long id) {
    	return flightDb.findById(id);
    }
    
    @Override
    public List<FlightModel> getAllFlights() {
    	return flightDb.findAll();
    }
}