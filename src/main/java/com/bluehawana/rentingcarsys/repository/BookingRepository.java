package com.bluehawana.rentingcarsys.repository;

import com.bluehawana.rentingcarsys.model.Booking;
import jakarta.annotation.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
