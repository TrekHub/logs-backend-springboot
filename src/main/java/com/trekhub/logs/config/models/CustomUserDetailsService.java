package com.trekhub.logs.config.models;

import com.trekhub.logs.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//make a service instead of just method to find user, good practice
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
      private final UserRepository userRepository;

      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("email not found"));
      }

}
