package com.qrjav.pmwebapp.controller;

import com.qrjav.pmwebapp.model.AuthValidationRequest;
import com.qrjav.pmwebapp.model.AuthenticationResponse;
import com.qrjav.pmwebapp.model.User;
import com.qrjav.pmwebapp.service.AuthenticationService;
import com.qrjav.pmwebapp.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    public AuthenticationController(AuthenticationService authService, JwtService jwtService, UserDetailsService userDetailsService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody User request) {
        System.out.println(request);
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/validate")
    public boolean validateToken(@RequestBody AuthValidationRequest authValidationRequest) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authValidationRequest.getUsername());
        return jwtService.isValid(authValidationRequest.getToken(), userDetails);
    }

    @PostMapping("/getUser")
    public User getUserDetailsFromToken(@RequestBody String token) {
        return authService.getUserDetailsFromToken(token);
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Integer id) {
        return authService.deleteUser(id);
    }

    @PutMapping("/editUser")
    public ResponseEntity<AuthenticationResponse> editUser(@RequestBody User request) {
        return ResponseEntity.ok(authService.editUser(request));
    }

    @GetMapping("/getUsersByCreator/{creatorUsername}")
    public List<User> getUsersByCreator(@PathVariable String creatorUsername) {
        return authService.getUsersByCreator(creatorUsername);
    }

    @GetMapping("/demo")
    public String demo() {
        return "Demo";
    }
}
