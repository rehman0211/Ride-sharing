package org.marehman.rideSharing.dao;

import org.marehman.rideSharing.model.Customer;
import org.marehman.rideSharing.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {


    Optional<Driver> findByAlias(String alias);
}
