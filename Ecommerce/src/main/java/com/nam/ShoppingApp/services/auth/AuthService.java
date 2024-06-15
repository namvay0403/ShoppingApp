package com.nam.ShoppingApp.services.auth;

import com.nam.ShoppingApp.dto.SignupRequest;
import com.nam.ShoppingApp.dto.UserDao;

public interface AuthService {

    UserDao createUser(SignupRequest signupRequest);

    boolean hasUserWithEmail(String email);
}
