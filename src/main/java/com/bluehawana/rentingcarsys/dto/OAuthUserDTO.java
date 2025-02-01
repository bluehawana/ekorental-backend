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
    private String picature;
    private String providerId;

    public OAuthUserDTO(String email, String name, String picture, String providerId) {
    }

    public String getProviderId() {
        return this.getProvider();
    }

    public String getPicture() {
        return this.getPicature();
    }
}


