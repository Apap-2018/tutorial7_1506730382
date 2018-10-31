package com.apap.tutorial6.service;

import static org.junit.Assert.assertThat;

import java.sql.Date;
import java.util.Optional;

import com.apap.tutorial6.model.FlightModel;
import com.apap.tutorial6.repository.FlightDb;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class FlightServiceTest {
	@Autowired
	private FlightService flightService;
	
	@MockBean
	private FlightDb flightDb;
	
	@TestConfiguration // untuk membatasi scope Bean yang didefinisikan menjadi local class
	static class CarServiceTestContextConfiguration {
		@Bean // untuk initiate flightService sebagai Bean
		public FlightService flightService() {
			return new FlightServiceImpl();
		}
	}
	
	@Test
	public void whenValidFlightNumber_thenFlightShouldBeFound() {
		// given
		FlightModel flightModel = new FlightModel();
		flightModel.setFlightNumber("X550");
		flightModel.setOrigin("Depok");
		flightModel.setDestination("Bekasi");
		flightModel.setTime(new Date(new java.util.Date().getTime()));
		Optional<FlightModel> flight = Optional.of(flightModel);
		Mockito.when(flightService.getFlightDetailByFlightNumber(flight.get().getFlightNumber())).thenReturn(flight);
		
		// when
		Optional<FlightModel> found = flightService.getFlightDetailByFlightNumber(flight.get().getFlightNumber());
		
		// then
		assertThat(found, Matchers.notNullValue());
		assertThat(found.get().getFlightNumber(), Matchers.equalTo(flightModel.getFlightNumber()));
	}
}
