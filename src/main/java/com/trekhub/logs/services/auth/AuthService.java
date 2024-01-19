package com.trekhub.logs.services.auth;


import com.trekhub.logs.config.JwtService;
import com.trekhub.logs.controllers.auth.AuthenticationRequest;
import com.trekhub.logs.controllers.auth.AuthenticationResponse;
import com.trekhub.logs.controllers.auth.RegisterRequest;
import com.trekhub.logs.dtos.UserRegistrationDTO;
import com.trekhub.logs.models.user.Role;
import com.trekhub.logs.models.user.User;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.trekhub.logs.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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

    public UserRegistrationDTO register (RegisterRequest request) {


        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())).role(Role.USER)
                .build();


        try {
            //check if the user already exists (email)
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return new UserRegistrationDTO("User Already exists");
            } else {
                //save user in db
                userRepository.save(user);
                //return a response to tell say user saved successfully
                //don't return a token here, you have to let the user authenticate before returning a token

                return new UserRegistrationDTO("saved successfully");
            }
        } catch (
                Exception ex
        ) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // find the user with email if they exist or throw exception and exit 
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new EntityNotFoundException("user not found"));
        System.out.println(user.getEmail());
       try {
        
         var authtoken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
       //if authentication fails you get an exception and exits
        Authentication auth = authenticationManager.authenticate(authtoken);
        //set the auth object inside the SecurityHolderContext available throughout the app
        if(auth.isAuthenticated()){
                var jwtToken = jwtService.generateToken(user);
                return  AuthenticationResponse
                        .builder()
                        .token(jwtToken)
                        .build();        
        }
        return null;
        
       } catch(Exception ex){
            System.out.println(ex.getMessage());
            throw new BadCredentialsException("bad credentials");
        }
        //generate a token that will be used to authenticate further incoming requests instead of requesting for email and password all the time
       }


}
