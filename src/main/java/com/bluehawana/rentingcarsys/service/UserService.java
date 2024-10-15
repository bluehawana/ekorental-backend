package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.model.User;
import java.util.List;

public interface UserService {
  org.apache.catalina.User createUser(org.apache.catalina.User user);

  User createUser(User user);
        User getUserById(Long id);
        List < User > getAllUsers();
        User updateUser(User user);
        void deleteUser(Long id);
        User findByEmail(String email);

    void createOrUpdateUser(String userName, String email);
}

