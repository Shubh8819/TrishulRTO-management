// src/main/java/com/licencemanagement/service/AuthService.java
package com.trishul.service;

import com.trishul.Model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    void register(User user);

    public void autoLogin(String email, String password,
                          HttpServletRequest request,
                          HttpServletResponse response);
    User getCurrentUser();
}