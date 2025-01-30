package com.bluehawana.rentingcarsys.dto;

import lombok.Data;

@Data
public class CarAvailabilityDTO {
    private boolean available;
    private String reason;
    // e.g., "BOOKED", "MAINTENANCE", "AVAILABLE"
}
