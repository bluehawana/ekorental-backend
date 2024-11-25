package com.bluehawana.rentingcarsys.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomErrorResponse {
    private String errorMessage;

    public CustomErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}