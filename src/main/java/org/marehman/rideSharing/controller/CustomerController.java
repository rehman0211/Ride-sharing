package org.marehman.rideSharing.controller;

import org.marehman.rideSharing.model.Customer;
import org.marehman.rideSharing.model.Driver;
import org.marehman.rideSharing.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @PostMapping("/customer")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer){
        return customerService.addNewCustomer(customer);
    }

    @PutMapping("/customer")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer){
        return customerService.updateCustomer(customer);
    }

    /*@Autowired
    ApplicationRepository applicationRepository;

    @GetMapping("/applications")
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @PostMapping("/applications")
    public Application createApplication(@RequestBody Application application) {
        return applicationRepository.save(application);
    }

    @GetMapping("/applications/{id}")
    public Application getApplicationById(@PathVariable(value = "id") Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver", "id", applicationId));
    }

    @PutMapping("/applications/{id}")
    public Application updateApplication(@PathVariable(value = "id") Long applicationId, @RequestBody Application applicationDetails) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver", "id", applicationId));

        application.setName(applicationDetails.getName());
        application.setDescription(applicationDetails.getDescription());

        Application updatedApplication = applicationRepository.save(application);
        return updatedApplication;
    }

    @DeleteMapping("/applications/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable(value = "id") Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver", "id", applicationId));

        applicationRepository.delete(application);

        return ResponseEntity.ok().build();
    }*/
}
