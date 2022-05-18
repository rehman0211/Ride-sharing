package org.marehman.rideSharing.service;


import org.marehman.rideSharing.dao.CustomerRepository;
import org.marehman.rideSharing.model.Customer;
import org.marehman.rideSharing.model.Driver;
import org.marehman.rideSharing.utils.CommonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("CustomerService")
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public ResponseEntity<?> addNewCustomer(Customer customer){
        customer.setUpdatedTime(System.currentTimeMillis());
        customer.setRegisteredTime(System.currentTimeMillis());
        customer.setAlias(CommonHelper.getMD5Encrypted(customer.getPhoneNumber() + customer.getRegisteredTime()));
        customer = customerRepository.save(customer);
        return new ResponseEntity(customer, HttpStatus.OK);
    }

    public ResponseEntity<?> updateCustomer(Customer customer){
        customer.setUpdatedTime(System.currentTimeMillis());
        customer = customerRepository.save(customer);
        return new ResponseEntity(customer, HttpStatus.OK);
    }
}
