package com.bluehawana.rentingcarsys.repository;

import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.model.BookingStatus;
import com.bluehawana.rentingcarsys.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser_Id(Long userId);
    List<Booking> findByUser(User user);
    List<Booking> findByCar_IdAndStatus(Long carId, BookingStatus status);

    // Add some useful queries
    @Query("SELECT b FROM Booking b WHERE b.car.id = :carId AND b.startTime <= :endDate AND b.endTime >= :startDate")
    List<Booking> findOverlappingBookings(@Param("carId") Long carId,
                                          @Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    List<Booking> findByStatusAndUser(BookingStatus status, User user);

    Optional<Booking> findByIdAndUser_Id(Long bookingId, Long userId);
}