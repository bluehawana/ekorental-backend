package com.bluehawana.rentingcarsys.repository;

import com.bluehawana.rentingcarsys.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Changed from findByUser to findByUserId
    List<Booking> findByUserId(Long userId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.carId = :carId " +
            "AND b.status != 'CANCELLED' " +
            "AND ((b.startTime BETWEEN :startTime AND :endTime) " +
            "OR (b.endTime BETWEEN :startTime AND :endTime) " +
            "OR (:startTime BETWEEN b.startTime AND b.endTime))")
    List<Booking> findOverlappingBookings(
            @Param("carId") Long carId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}