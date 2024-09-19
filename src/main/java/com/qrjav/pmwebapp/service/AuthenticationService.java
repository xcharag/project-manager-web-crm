package com.qrjav.pmwebapp.service;

import com.qrjav.pmwebapp.model.AuthenticationResponse;
import com.qrjav.pmwebapp.model.User;
import com.qrjav.pmwebapp.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User request){
        User user = new User();
        System.out.println(request);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setCreatorUsername(request.getCreatorUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(User request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }

    public User getUserDetailsFromToken(String token) {
        String username = jwtService.extractUsername(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public AuthenticationResponse editUser(User request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        User checkedUser = checkNewUserValues(user, request);
        userRepository.save(checkedUser);
        String token = jwtService.generateToken(checkedUser);

        return new AuthenticationResponse(token);
    }

    private User checkNewUserValues(User user, User request) {
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        return user;
    }

    public List<User> getUsersByCreator(String creatorUsername) {
        return userRepository.findByCreatorUsername(creatorUsername);
    }

    public String deleteUser(Integer id) {
        userRepository.deleteById(id);
        return "User deleted";
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
