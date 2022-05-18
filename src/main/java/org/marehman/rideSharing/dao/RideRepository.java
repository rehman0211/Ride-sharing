package org.marehman.rideSharing.dao;

import org.marehman.rideSharing.model.Customer;
import org.marehman.rideSharing.model.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
//
//    @Query(value = "SELECT r FROM RideShare_Ride r where :query")
//    Page<Ride> findByCustomQuery(@Param("query")String query, Pageable pageable);

    Optional<Ride> findByAlias(String alias);

    Page<Ride> findAll(Specification<Ride> specifications, Pageable pageable);
}
