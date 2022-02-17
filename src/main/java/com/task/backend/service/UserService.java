package com.task.backend.service;

import com.task.backend.payload.request.LoginRequest;
import com.task.backend.payload.request.SignupRequest;
import com.task.backend.payload.response.JWTResponseToken;
import javassist.NotFoundException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

public interface UserService {

    Boolean checkUserNameExist(String userName);

    Boolean checkEmailExist(String email);

    JWTResponseToken signupUser(SignupRequest signUpRequest) throws ServiceException;

    JWTResponseToken signinUser(LoginRequest loginRequest) throws AuthenticationCredentialsNotFoundException;

    void removeUserByUserName(String userName) throws ServiceException, NotFoundException;

}
