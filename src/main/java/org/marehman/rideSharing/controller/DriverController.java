package org.marehman.rideSharing.controller;

import org.marehman.rideSharing.dao.DriverRepository;
import org.marehman.rideSharing.model.Driver;
import org.marehman.rideSharing.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drivers")
public class DriverController {
    @Autowired
    DriverService driverService;

    @PostMapping("/driver")
    public ResponseEntity<?> addDriver(@RequestBody Driver driver){
        return driverService.addNewDriver(driver);
    }
}
