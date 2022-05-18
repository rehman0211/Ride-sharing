package org.marehman.rideSharing.service;


import org.marehman.rideSharing.dao.CustomerRepository;
import org.marehman.rideSharing.dao.DriverRepository;
import org.marehman.rideSharing.model.Driver;
import org.marehman.rideSharing.utils.CommonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("DriverService")
public class DriverService {

    @Autowired
    DriverRepository driveRepository;

    public ResponseEntity<?> addNewDriver(Driver driver){
        driver.setRatings(5.0);
        driver.setUpdatedTime(System.currentTimeMillis());
        driver.setRegisteredTime(System.currentTimeMillis());
        driver.setAlias(CommonHelper.getMD5Encrypted(driver.getPhoneNumber() + driver.getRegisteredTime()));
        driver = driveRepository.save(driver);
        return new ResponseEntity(driver, HttpStatus.OK);
    }

    public Driver setDriverRatings(Driver driver, int rating){
        if(driver!=null){
            Double driverRatings = driver.getRatings();
            driverRatings = (driverRatings*driver.getNoOfRides() + rating)/(driver.getNoOfRides()+1);
            driver.setRatings(driverRatings);
            driver.incrementNoOfRides();
        }
        return driver;
    }
}
