package com.bluehawana.rentingcarsys.mapper;

import com.bluehawana.rentingcarsys.dto.BookingResponseDTO;
import com.bluehawana.rentingcarsys.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingResponseDTO toDTO(Booking booking) {
        return new BookingResponseDTO(
                booking.getId(),
                booking.getUser().getId(),
                booking.getCar().getId(),
                booking.getStartTime().toString(),
                booking.getEndTime().toString(),
                booking.getStatus().toString(),
                booking.getTotalPrice().toString()
        );
    }
}