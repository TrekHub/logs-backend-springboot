package com.trekhub.logs.services.auth;


import com.trekhub.logs.config.JwtService;
import com.trekhub.logs.controllers.auth.AuthenticationRequest;
import com.trekhub.logs.controllers.auth.AuthenticationResponse;
import com.trekhub.logs.controllers.auth.RegisterRequest;
import com.trekhub.logs.models.user.Role;
import com.trekhub.logs.models.user.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.trekhub.logs.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register (RegisterRequest request){


        var user = User.builder()
        .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())).role(Role.USER)
                .build();



        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return  AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return  AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }


}
