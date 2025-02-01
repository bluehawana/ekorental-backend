package com.bluehawana.rentingcarsys.util;

import com.google.common.io.Files;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    public static Long getCurrentUserId() {
        // For now, return a default user ID (you'll implement proper security later)
        return 1L;
    }

    public static Files getCurrentUser() {
        return getCurrentUser();
    }
}