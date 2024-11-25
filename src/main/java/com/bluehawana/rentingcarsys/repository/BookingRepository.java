package com.bluehawana.rentingcarsys.repository;

import com.bluehawana.rentingcarsys.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCar_Id(Long carId);
    List<Booking> findByUserEmail(String userEmail);
}