package com.bluehawana.rentingcarsys.repository;

import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b JOIN b.user u WHERE u.email = :email")
    List<Booking> findByUserEmail(@Param("email") String email);

    List<Booking> findByUser(User user);
}