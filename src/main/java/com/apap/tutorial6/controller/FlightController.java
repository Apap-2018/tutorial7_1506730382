package com.apap.tutorial6.controller;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.sql.Date;

import javax.servlet.http.HttpServletRequest;

import com.apap.tutorial6.model.FlightModel;
import com.apap.tutorial6.model.PilotModel;
import com.apap.tutorial6.rest.PilotDetail;
import com.apap.tutorial6.rest.Setting;
import com.apap.tutorial6.service.FlightService;
import com.apap.tutorial6.service.PilotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * FlightController
 */

@RestController
@RequestMapping("/flight")
public class FlightController {
	@Autowired
	private FlightService flightService;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Bean
	public RestTemplate rest() {
		return new RestTemplate();
	}
	
	@PostMapping(value = "/add")
	public FlightModel addFlightSubmit(@RequestBody FlightModel flight) {
		return flightService.addFlight(flight);
	}
	
	@GetMapping(value = "/view/{flightNumber}")
	public FlightModel flightView(@PathVariable("flightNumber") String flightNumber) {
		FlightModel flight = flightService.getFlightDetailByFlightNumber(flightNumber).get();
		return flight;
	}
	
	@GetMapping(value = "/all")
	public List<FlightModel> flightViewAll() {
		List<FlightModel> flightList = flightService.getAllFlights();
		return flightList;
	}
	
	@DeleteMapping(value = "/delete")
	public String deleteFlight(@RequestParam("flightId") long flightId) {
		FlightModel flight = flightService.getFlightDetailById(flightId).get();
		flightService.deleteByFlightNumber(flight.getFlightNumber());
		return "flight has been deleted";
	}
	
	@PutMapping(value = "/update/{flightId}") 
	public String updateFlightSubmit(@PathVariable("flightId") long flightId,
									@RequestParam("destination") Optional<String> destination,
									@RequestParam("origin") Optional<String> origin,
									@RequestParam("time") Optional<Date> time) {
		FlightModel flight = flightService.getFlightDetailById(flightId).get();
		if (flight.equals(null)) {
			return "Couldn't find the flight";
		} else {
			if (destination.isPresent()) {
				flight.setDestination(destination.get());
			}
			if (origin.isPresent()) {
				flight.setOrigin(origin.get());
			}
			if (time.isPresent()) {
				flight.setTime(time.get());
			}
			flightService.addFlight(flight);
			return "flight update success";
		}
	}
}
	
//	@PutMapping(value = "/update/{pilotId}") 
//	public String updatePilotSubmit(@PathVariable("pilotId") long pilotId,
//									@RequestParam("name") String name,
//									@RequestParam("flyHour") int flyHour) {
//		PilotModel pilot = pilotService.getPilotDetailById(pilotId).get();
//		if (pilot.equals(null)) {
//			return "Couldn't find your pilot";
//		}
//		
//		pilot.setName(name);
//		pilot.setFlyHour(flyHour);
//		pilotService.addPilot(pilot);
//		return "update";
//	}
	
//	@GetMapping(value = "/status/{licenseNumber}")
//	public String getStatus(@PathVariable("licenseNumber") String licenseNumber) throws Exception {
//		String path = Setting.pilotUrl + "/pilot?licenseNumber=" + licenseNumber;
//		return restTemplate.getForEntity(path, String.class).getBody();
//	}
//	
//	@GetMapping(value = "/full/{licenseNumber}")
//	public PilotDetail postStatus(@PathVariable("licenseNumber") String licenseNumber) throws Exception {
//		String path = Setting.pilotUrl + "/pilot";
//		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber).get();
//		PilotDetail detail = restTemplate.postForObject(path, pilot, PilotDetail.class);
//		return detail;
//	}


/**
@Controller
public class FlightController {
    @Autowired
    private FlightService flightService;
    
    @Autowired
    private PilotService pilotService;

    @RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.GET)
    private String add(@PathVariable(value = "licenseNumber") String licenseNumber, Model model) {
        PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber).get();
        pilot.setListFlight(new ArrayList<FlightModel>(){
            private ArrayList<FlightModel> init(){
                this.add(new FlightModel());
                return this;
            }
        }.init());

        model.addAttribute("pilot", pilot);
        return "add-flight";
    }

    @RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.POST, params={"addRow"})
    private String addRow(@ModelAttribute PilotModel pilot, Model model) {
        pilot.getListFlight().add(new FlightModel());
        model.addAttribute("pilot", pilot);
        return "add-flight";
    }

    @RequestMapping(value="/flight/add/{licenseNumber}", method = RequestMethod.POST, params={"removeRow"})
    public String removeRow(@ModelAttribute PilotModel pilot, Model model, HttpServletRequest req) {
        Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
        pilot.getListFlight().remove(rowId.intValue());
        
        model.addAttribute("pilot", pilot);
        return "add-flight";
    }

    @RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.POST, params={"save"})
    private String addFlightSubmit(@ModelAttribute PilotModel pilot) {
        PilotModel archive = pilotService.getPilotDetailByLicenseNumber(pilot.getLicenseNumber()).get();
        for (FlightModel flight : pilot.getListFlight()) {
            if (flight != null) {
                flight.setPilot(archive);
                flightService.addFlight(flight);
            }
        }
        return "add";
    }


    @RequestMapping(value = "/flight/view", method = RequestMethod.GET)
    private @ResponseBody FlightModel view(@RequestParam(value = "flightNumber") String flightNumber, Model model) {
        FlightModel archive = flightService.getFlightDetailByFlightNumber(flightNumber).get();
        return archive;
    }

    @RequestMapping(value = "/flight/delete", method = RequestMethod.POST)
    private String delete(@ModelAttribute PilotModel pilot, Model model) {
        for (FlightModel flight : pilot.getListFlight()) {
            flightService.deleteByFlightNumber(flight.getFlightNumber());
        }
        return "delete";
    }

    @RequestMapping(value = "/flight/update", method = RequestMethod.GET)
    private String update(@RequestParam(value = "flightNumber") String flightNumber, Model model) {
        FlightModel archive = flightService.getFlightDetailByFlightNumber(flightNumber).get();
        model.addAttribute("flight", archive);
        return "update-flight";
    }

    @RequestMapping(value = "/flight/update", method = RequestMethod.POST)
    private @ResponseBody FlightModel updateFlightSubmit(@ModelAttribute FlightModel flight, Model model) {
        flightService.addFlight(flight);
        return flight;
    }
}
*/