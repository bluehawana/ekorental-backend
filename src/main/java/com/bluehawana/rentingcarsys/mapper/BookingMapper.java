package com.bluehawana.rentingcarsys.mapper;

import com.bluehawana.rentingcarsys.dto.BookingResponseDTO;
import com.bluehawana.rentingcarsys.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingResponseDTO toDTOBuilder(Booking booking) {
        return BookingResponseDTO.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .carId(booking.getCar().getId())
                .startDate(booking.getStartTime())
                .endDate(booking.getEndTime())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}