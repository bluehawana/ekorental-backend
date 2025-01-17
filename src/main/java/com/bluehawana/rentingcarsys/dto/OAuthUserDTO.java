package com.bluehawana.rentingcarsys.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthUserDTO {
    private String email;
    private String name;
    private String provider;
    private String avatarUrl;
    private String role;
}


