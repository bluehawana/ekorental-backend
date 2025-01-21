package com.bluehawana.rentingcarsys.dto;

import com.bluehawana.rentingcarsys.model.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingResponseDTO {
    private Long id;
    private Long userId;
    private Long carId;
    private String startTime;
    private String endTime;
    private String status;
    private String totalPrice;

    public BookingResponseDTO(Booking booking) {
        this.id = booking.getId();
        this.userId = booking.getUser().getId();
        this.carId = booking.getCar().getId();
        this.startTime = booking.getStartTime().toString();
        this.endTime = booking.getEndTime().toString();
        this.status = booking.getStatus().toString();
        this.totalPrice = booking.getTotalPrice().toString();
    }
}