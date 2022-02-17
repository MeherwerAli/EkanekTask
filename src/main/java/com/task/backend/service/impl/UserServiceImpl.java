package com.task.backend.service.impl;

import com.task.backend.model.User;
import com.task.backend.payload.request.LoginRequest;
import com.task.backend.payload.request.SignupRequest;
import com.task.backend.payload.response.JWTResponseToken;
import com.task.backend.repository.UserRepository;
import com.task.backend.security.jwt.JwtUtils;
import com.task.backend.service.UserService;
import com.task.backend.util.AppConstants;
import javassist.NotFoundException;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public Boolean checkUserNameExist(String userName) {

        return userRepository.existsByUserName(userName);
    }

    @Override
    public Boolean checkEmailExist(String email) {

        return userRepository.existsByEmail(email);
    }

    @Override
    public JWTResponseToken signupUser(SignupRequest signUpRequest) throws ServiceException {
        try {
            if (signUpRequest.getPassword() == null)
                return new JWTResponseToken(null, null, AppConstants.ERROR_EMPTY_PASSWORD);
            else if (signUpRequest.getEmail() == null)
                return new JWTResponseToken(null, null, AppConstants.ERROR_EMPTY_EMAIL);
            else if (signUpRequest.getUsername() == null)
                return new JWTResponseToken(null, null, AppConstants.ERROR_EMPTY_USERNAME);
            User user = new User(
                    signUpRequest.getFirstName(),
                    signUpRequest.getLastName(),
                    signUpRequest.getUsername(),
                    signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()));

            user = userRepository.save(user);
            JWTResponseToken response = authenticateUser(new LoginRequest(user.getUserName(), signUpRequest.getPassword()));
            response.setMessage(AppConstants.MESSAGE_SIGNUP_SUCCESSFUL);
            logger.info(AppConstants.MESSAGE_SIGNUP_SUCCESSFUL);
            return response;
        } catch (ServiceException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public JWTResponseToken signinUser(LoginRequest loginRequest) throws AuthenticationCredentialsNotFoundException {
        try {
//            if(true){
//                Optional<User> user =userRepository.findByEmail(loginRequest.getEmail());
//                loginRequest.setUsername(user.get().getUserName());
//            }
            JWTResponseToken response = authenticateUser(loginRequest);
            response.setMessage(AppConstants.MESSAGE_SIGNIN_SUCCESSFUL);
            logger.info(AppConstants.MESSAGE_SIGNIN_SUCCESSFUL);
            return response;
        } catch (AuthenticationCredentialsNotFoundException ex) {
            throw new AuthenticationCredentialsNotFoundException(ex.getMessage());
        }
    }

    @Override
    public void removeUserByUserName(String userName) throws NotFoundException {
        try {
            Optional<User> optionalUser = userRepository.findByUserName(userName);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                userRepository.delete(user);
            }
        } catch (Exception ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(AppConstants.USERNAME_NOT_FOUND + userName));

        return UserDetailsImpl.build(user);
    }

    public JWTResponseToken authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        @SuppressWarnings("unused")
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails.getUsername());

        return new JWTResponseToken(userDetails.getUsername(), jwtToken);
    }
}
