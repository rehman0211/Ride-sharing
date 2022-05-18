package org.marehman.rideSharing.controller;


import org.marehman.rideSharing.service.RideService;
import org.marehman.rideSharing.utils.CommonHelper;
import org.marehman.rideSharing.utils.FilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Map;

@RestController
@RequestMapping("/ride")
public class RideController {

    @Autowired
    RideService rideService;

    @PostMapping("/new")
    public ResponseEntity<?> inititeRide(@RequestBody Map<String, Object> requestBody) {
        return CommonHelper.buildResponseEntity(rideService.createNewRide(requestBody));
    }

    @PostMapping("/{rideId}/accept-ride")
    public ResponseEntity<?> initiateRide(@PathVariable("rideId") String rideId, @RequestBody Map<String, Object> requestBody) {
        return CommonHelper.buildResponseEntity(rideService.acceptRide(rideId, requestBody));
    }

    @PostMapping("/{rideId}/update-ride-status")
    public ResponseEntity<?> updateRideStatus(@PathVariable("rideId") String rideId, @RequestBody Map<String, Object> requestBody) {
        return CommonHelper.buildResponseEntity(rideService.updateRideStatus(rideId, requestBody));
    }

    @PostMapping("/rides")
    public ResponseEntity<?> updateRideStatus(@RequestBody FilterRequest filterRequest, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int pageSize) {
        return CommonHelper.buildResponseEntity(rideService.getRidesByFilter(filterRequest, page, pageSize));
    }
}
