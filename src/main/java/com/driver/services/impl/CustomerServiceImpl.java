package com.driver.services.impl;

import com.driver.model.TripBooking;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;
import com.driver.model.TripStatus;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		if(customerRepository2.findById(customerId).isPresent()){
			Customer customer = customerRepository2.findById(customerId).get();
			customerRepository2.delete(customer);
		}
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		List<Driver> driverList = driverRepository2.findAll();
		Driver minDriver = null;
		for(Driver driver: driverList){
			if(minDriver == null && driver.getCab().getAvailable()){
				minDriver = driver;
			}
			if(minDriver!=null && driver.getCab().getAvailable() && driver.getDriverId() < minDriver.getDriverId()){
				minDriver = driver;
			}
		}
		if(minDriver == null) throw new Exception("No cab available!");

		TripBooking tripBooking = new TripBooking(fromLocation,toLocation,distanceInKm,TripStatus.CONFIRMED);

		Customer customer = customerRepository2.findById(customerId).get();

		minDriver.getCab().setAvailable(false);

		tripBooking.setCustomer(customer);
		tripBooking.setDriver(minDriver);

		tripBooking.setBill(distanceInKm * minDriver.getCab().getPerKmRate());

		customer.getTripBookingList().add(tripBooking);
		minDriver.getTripBookingList().add(tripBooking);


		driverRepository2.save(minDriver);
		customerRepository2.save(customer);

		return tripBooking;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		if(tripBookingRepository2.findById(tripId).isPresent()) {
			TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
			tripBooking.setBill(0);
			tripBooking.getDriver().getCab().setAvailable(true);
			tripBooking.setStatus(TripStatus.CANCELED);
			driverRepository2.save(tripBooking.getDriver());
		}
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		if(tripBookingRepository2.findById(tripId).isPresent()) {
			TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
			tripBooking.getDriver().getCab().setAvailable(true);
			tripBooking.setStatus(TripStatus.COMPLETED);
			driverRepository2.save(tripBooking.getDriver());
		}
	}
}
