package org.marehman.rideSharing.service;

import org.marehman.rideSharing.dao.CustomerRepository;
import org.marehman.rideSharing.dao.DriverRepository;
import org.marehman.rideSharing.dao.RideRepository;
import org.marehman.rideSharing.model.Customer;
import org.marehman.rideSharing.model.Driver;
import org.marehman.rideSharing.model.Ride;
import org.marehman.rideSharing.utils.CommonHelper;
import org.marehman.rideSharing.utils.Filter;
import org.marehman.rideSharing.utils.FilterRequest;
import org.marehman.rideSharing.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("RideService")
public class RideService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    DriverRepository driverRepository;
    @Autowired
    RideRepository rideRepository;
    @Autowired
    DriverService driverService;

    public Response createNewRide(Map<String, Object> requestBody) {
        String pickupLocation = CommonHelper.getStringValue(requestBody.get("pickup_location"));
        String dropLocation = CommonHelper.getStringValue(requestBody.get("drop_location"));
        String customerId = CommonHelper.getStringValue(requestBody.get("customer_id"));
        Customer customer = customerRepository.findByAlias(customerId).orElse(null);
        if (customer == null) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Customer, please register first!!!");
        }
        Long bookTime = System.currentTimeMillis();
        Double amount = getRideAmount(pickupLocation, dropLocation, bookTime);
        //or some other algorithm
        Ride ride = new Ride(pickupLocation, dropLocation, amount, "initiated", customer);
        ride.setAlias(CommonHelper.getMD5Encrypted(bookTime + customerId + amount + pickupLocation + dropLocation));
        ride = rideRepository.save(ride);
        if (ride == null) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Some internal issue in initate the ride, please try again!!!");
        }
        return new Response(HttpStatus.OK, ride);
    }

    private Double getRideAmount(String pickupLocation, String dropLocation, Long bookTime) {
        //we can introduce some algorithm for charging amount
        //based on pickup, drop and time
        //for now, taking some random amount bw 300&1000
        return Math.random() * (700) + 300;
    }

    public Response acceptRide(String rideId, Map<String, Object> requestBody) {
        String driverId = CommonHelper.getStringValue(requestBody.get("driverId"));
        Driver driver = driverRepository.findByAlias(driverId).orElse(null);
        Ride ride = rideRepository.findByAlias(rideId).orElse(null);
        if (driver == null || ride == null) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid inputs, please verify first!!!");
        }
        if (driver.getBooked()) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "You are already booked!!!, first complete your ride !!!");
        }
        if (ride.getStatus().equalsIgnoreCase("initiated")) {
            driver.setBooked(true);
            ride.setStatus("accepted");
            ride.setAcceptTime(System.currentTimeMillis());
            ride.setDriver(driver);
            ride.setDriverAlias(driver.getAlias());
            ride = rideRepository.save(ride);
            if (ride == null) {
                return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Some internal issue in accepting the ride, please try again!!!");
            }
            return new Response(HttpStatus.OK, ride);
        } else {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Sorry, could not accept. Ride status is changed to: " + ride.getStatus());
        }
    }

    public Response updateRideStatus(String rideId, Map<String, Object> requestBody) {
        String status = CommonHelper.getStringValue(requestBody.get("status"));
        Ride ride = rideRepository.findByAlias(rideId).orElse(null);
        if (ride == null) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid ride Id, please verify first!!!");
        }
        if (status.equalsIgnoreCase("cancel")) {
            ride.setStatus(status);
            ride.setCancelTime(System.currentTimeMillis());
        } else if (status.equalsIgnoreCase("start")) {
            ride.setStatus(status);
            ride.setStartTime(System.currentTimeMillis());
        } else if (status.equalsIgnoreCase("completed")) {
            int ratings = CommonHelper.getIntegerValue(requestBody.getOrDefault("ratings", "5"));
            ride.setStatus(status);
            ride.setRating(ratings);
            Driver driver = ride.getDriver();
            driver = driverService.setDriverRatings(driver, ratings);
            driver.setBooked(false);
            ride.setDriver(driver);
            ride.setEndTime(System.currentTimeMillis());
            ride.setTravelTime(ride.getEndTime() - ride.getStartTime());
        } else {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Status chosen!!!");
        }
        ride = rideRepository.save(ride);
        if (ride == null) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Some internal issue in accepting the ride, please try again!!!");
        }
        Ride ride1 = new Ride();
        return new Response(HttpStatus.OK, ride);
    }

    public Response getRidesByFilter(FilterRequest requestBody, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<Filter> filters = new ArrayList<>();
        String customerId = requestBody.getCustomerId();
        String driverId = requestBody.getDriverId();
        if (customerId != null) {
            Customer customer = customerRepository.findByAlias(customerId).orElse(null);
            if (customer == null) {
                return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Some internal issue in accepting the ride, please try again!!!");
            }
            filters.add(new Filter("customerAlias", CommonHelper.getStringValue(customerId), "="));
        }
        if (driverId != null) {
            Driver driver = driverRepository.findByAlias(driverId).orElse(null);
            if (driver == null) {
                return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Some internal issue in accepting the ride, please try again!!!");
            }
            filters.add(new Filter("driverAlias", CommonHelper.getStringValue(driverId), "="));
        }
        List<Filter> givenFilters = requestBody.getFilters();
        filters.addAll(givenFilters);
        //Page<Ride> rideList = rideRepository.findByCustomQuery(customQuery(filters), pageable);

        String condition = requestBody.getCondition() != null ? requestBody.getCondition() : "AND";

        Page<Ride> rideList = rideRepository.findAll(getSpecifications(filters, condition), pageable);
        return new Response(HttpStatus.OK, rideList.getContent(), rideList.getNumber(), rideList.getTotalElements(), rideList.getTotalPages());
        //return new Response();
    }

    public String customQuery(List<Filter> filters) {
        String query = "WHERE ";
        for (int i = 0; i < filters.size(); i++) {
            Filter filter = filters.get(i);
            query += filter.field + filter.comparator + filter.value;
            if (i != filters.size() - 1) {
                query += " AND ";
            }
        }
        return query;
    }


    public static Specification<Ride> getSpecifications(List<Filter> filters, String condition) {

        Specification<Ride> specification = new Specification<Ride>() {
            @Override
            public Predicate toPredicate(Root<Ride> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();

                for (Filter filter : filters) {
                    if (filter.comparator.equalsIgnoreCase("=")) {
                        Predicate predicate = cb.equal(root.get(filter.field), filter.value);
                        list.add(predicate);
                    } else if (filter.comparator.equalsIgnoreCase("<=")) {
                        Predicate predicate = cb.le(root.get(filter.field), (Number) filter.value);
                        list.add(predicate);
                    } else if (filter.comparator.equalsIgnoreCase(">=")) {
                        Predicate predicate = cb.ge(root.get(filter.field), (Number) filter.value);
                        list.add(predicate);
                    } else if (filter.comparator.equalsIgnoreCase("like")) {
                        Predicate predicate = cb.like(root.get(filter.field), "%" + CommonHelper.getStringValue(filter.value) + "%");
                        list.add(predicate);
                    }
                }

//                //cell sid condition
//                String apartmentSid = StringUtil.getStr(filters.get("apartmentSid"));
//                if(!"".equals(apartmentSid)){
//                    Predicate apartmentSidPre = cb.equal(root.get("apartmentSid"), apartmentSid);
//                    list.add(apartmentSidPre);
//                }
//
//                         / / Service category conditions
//                String serviceCategory = StringUtil.getStr(param.get("serviceCategory"));
//                if(!"".equals(serviceCategory)){
//                    List<String> serviceCategoryList = StringUtil.getStrList(serviceCategory, ",");
//                    In<String> in = cb.in(root.get("serviceCategory"));
//                    for (String sc : serviceCategoryList) {
//                        in.value(sc);
//                    }
//                    list.add(in);
//                }
//
//                //The status is 1
//                Predicate status1Pre = cb.equal(root.get("serviceStatus"), "1");
//
//                //The status is in 2,20,21,22
//                List<String> statusList = StringUtil.getStrList("2,20,21,22", ",");
//                In<String> statusIn = cb.in(root.get("serviceStatus"));
//                for (String status : statusList) {
//                    statusIn.value(status);
//                }
//
//                //The status is 6
//                Predicate status6Pre = cb.equal(root.get("serviceStatus"), "6");
//                //Evaluation total score: A.EVALUATION_ITEM3 + A.EVALUATION_ITEM2 + A.EVALUATION_ITEM1
//                Expression<Integer> evaluationItemSum = cb.sum(cb.sum(root.get("evaluationItem1"), root.get("evaluationItem2")), root.get("evaluationItem3"));
//                         / / Evaluation total score is less than 12
//                Predicate evaluationItemPre = cb.lessThan(evaluationItemSum, 12);
//                //The status is 6 and the total score is less than 12
//                Predicate or3Pre = cb.and(status6Pre, evaluationItemPre);
//
//                Predicate statusPre = cb.or(status1Pre, statusIn, or3Pre);
//                list.add(statusPre);
                if(condition.equalsIgnoreCase("OR")) {
                    return cb.or(list.toArray(new Predicate[list.size()]));
                }
                return cb.and(list.toArray(new Predicate[list.size()]));
            }
        };

        return specification;
    }

    ///////////////// For Filter -- Some Samples /////////////

    //checking with some variables with likely same value
//    public static Specification<User> containsTextInName(String text) {
//        if (!text.contains("%")) {
//            text = "%" + text + "%";
//        }
//        String finalText = text;
//        return (root, query, builder) -> builder.or(
//                builder.like(root.get("lastname"), finalText),
//                builder.like(root.get("firstname"), finalText)
//        );
//    }

    //checking with List of variables with same values
//    public static Specification<User> containsTextInAttributes(String text, List<String> attributes) {
//        if (!text.contains("%")) {
//            text = "%" + text + "%";
//        }
//        String finalText = text;
//        return (root, query, builder) -> builder.or(root.getModel().getDeclaredSingularAttributes().stream()
//                .filter(a -> attributes.contains(a.getName()))
//                .map(a -> builder.like(root.get(a.getName()), finalText))
//                .toArray(Predicate[]::new)
//        );
//    }
}
