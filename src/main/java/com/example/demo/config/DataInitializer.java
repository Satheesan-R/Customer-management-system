package com.example.demo.config;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeCountries();
        initializeCities();
        initializeSampleCustomers();
    }

    private void initializeCountries() {
        if (countryRepository.count() == 0) {
            Country[] countries = {
                createCountry("Sri Lanka", "LK"),
                createCountry("India", "IN"),
                createCountry("United States", "US"),
                createCountry("United Kingdom", "UK"),
                createCountry("Australia", "AU")
            };
            
            for (Country country : countries) {
                countryRepository.save(country);
            }
            System.out.println("✅ Sample countries created");
        }
    }

    private void initializeCities() {
        if (cityRepository.count() == 0) {
            Country sriLanka = countryRepository.findByName("Sri Lanka");
            Country india = countryRepository.findByName("India");
            Country usa = countryRepository.findByName("United States");
            Country uk = countryRepository.findByName("United Kingdom");
            Country australia = countryRepository.findByName("Australia");

            if (sriLanka != null) {
                cityRepository.save(createCity("Colombo", sriLanka));
                cityRepository.save(createCity("Kandy", sriLanka));
                cityRepository.save(createCity("Galle", sriLanka));
            }
            
            if (india != null) {
                cityRepository.save(createCity("Mumbai", india));
                cityRepository.save(createCity("Delhi", india));
                cityRepository.save(createCity("Bangalore", india));
            }
            
            if (usa != null) {
                cityRepository.save(createCity("New York", usa));
                cityRepository.save(createCity("Los Angeles", usa));
            }
            
            if (uk != null) {
                cityRepository.save(createCity("London", uk));
                cityRepository.save(createCity("Manchester", uk));
            }
            
            if (australia != null) {
                cityRepository.save(createCity("Sydney", australia));
                cityRepository.save(createCity("Melbourne", australia));
            }
            
            System.out.println("✅ Sample cities created");
        }
    }

    private void initializeSampleCustomers() {
        if (customerRepository.count() == 0) {
            City colombo = cityRepository.findByName("Colombo");
            City kandy = cityRepository.findByName("Kandy");
            City mumbai = cityRepository.findByName("Mumbai");
            City newYork = cityRepository.findByName("New York");
            City london = cityRepository.findByName("London");

            if (colombo != null) {
                createSampleCustomer("John Silva", LocalDate.of(1990, 5, 15), "199012345V", colombo, "0771234567", "0712345678");
                createSampleCustomer("Sarah Perera", LocalDate.of(1985, 8, 22), "198567890V", colombo, "0772345678", "0713456789");
            }
            
            if (kandy != null) {
                createSampleCustomer("Raj Fernando", LocalDate.of(1992, 3, 10), "199234567V", kandy, "0773456789", "0714567890");
            }
            
            if (mumbai != null) {
                createSampleCustomer("Priya Sharma", LocalDate.of(1988, 12, 5), "198845678V", mumbai, "0774567890", "0715678901");
            }
            
            if (newYork != null) {
                createSampleCustomer("Mike Johnson", LocalDate.of(1991, 7, 18), "199156789V", newYork, "0775678901", "0716789012");
            }
            
            if (london != null) {
                createSampleCustomer("Emma Wilson", LocalDate.of(1989, 9, 25), "198967890V", london, "0776789012", "0717890123");
            }
            
            System.out.println("✅ Sample customers created");
        }
    }

    private Country createCountry(String name, String code) {
        Country country = new Country();
        country.setName(name);
        country.setCode(code);
        return country;
    }

    private City createCity(String name, Country country) {
        City city = new City();
        city.setName(name);
        city.setCountry(country);
        return city;
    }

    private void createSampleCustomer(String name, LocalDate dob, String nic, City city, String mobile1, String mobile2) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setDob(dob);
        customer.setNic(nic);
        
        // Add mobile numbers
        Set<MobileNumber> mobileNumbers = new HashSet<>();
        
        MobileNumber mobile = new MobileNumber();
        mobile.setNumber(mobile1);
        mobile.setCustomer(customer);
        mobileNumbers.add(mobile);
        
        if (mobile2 != null) {
            MobileNumber mobile2Entity = new MobileNumber();
            mobile2Entity.setNumber(mobile2);
            mobile2Entity.setCustomer(customer);
            mobileNumbers.add(mobile2Entity);
        }
        
        customer.setMobileNumbers(mobileNumbers);
        
        // Add address
        Address address = new Address();
        address.setLine1("123 Main Street");
        address.setLine2("Apt 4B");
        address.setCity(city);
        address.setCustomer(customer);
        
        Set<Address> addresses = new HashSet<>();
        addresses.add(address);
        customer.setAddresses(addresses);
        
        customerRepository.save(customer);
    }
}
