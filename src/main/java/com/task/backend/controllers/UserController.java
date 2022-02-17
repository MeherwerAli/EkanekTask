package com.task.backend.controllers;


import com.task.backend.payload.request.LoginRequest;
import com.task.backend.payload.request.SignupRequest;
import com.task.backend.payload.response.JWTResponseToken;
import com.task.backend.security.jwt.JwtUtils;
import com.task.backend.service.UserService;
import com.task.backend.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${jwt.cookie}")
    private String jwtCookie;

    @PostMapping("/auth/signin")
    public ResponseEntity<JWTResponseToken> signInUser(@Valid @RequestBody LoginRequest loginRequest) {
        JWTResponseToken response = userService.signinUser(loginRequest);
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, response.getJwtToken()).path("/").maxAge(24 * 60 * 60).httpOnly(true).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(response);
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<JWTResponseToken> signUpUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (userService.checkUserNameExist(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new JWTResponseToken(null, null, AppConstants.USERNAME_ALREADY_TAKEN));
        }

        if (userService.checkEmailExist(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new JWTResponseToken(null, null, AppConstants.EMAIL_ALREADY_IN_USE));
        }

        // Create new user's account
        return ResponseEntity.ok(userService.signupUser(signUpRequest));
    }


    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("You've been signed out!");
    }

    @PutMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody String userName) throws Exception {
        userService.removeUserByUserName(userName);
        return ResponseEntity.ok("user deleted!!!");
    }

}
