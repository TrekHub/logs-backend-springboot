package com.trekhub.logs.controllers.auth;

import com.trekhub.logs.dtos.UserRegistrationDTO;
import com.trekhub.logs.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService service;


    @PostMapping("/register")
    public ResponseEntity<UserRegistrationDTO> register(@RequestBody RegisterRequest request){
        return  ResponseEntity.ok(service.register(request));

    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){

        return  ResponseEntity.ok(service.authenticate(request));

    }
    //a restricted route
     @GetMapping("/greetings")
    public ResponseEntity<String> greetings(){

        return  ResponseEntity.ok("Hello from a restricted endpoint");

    }

}
