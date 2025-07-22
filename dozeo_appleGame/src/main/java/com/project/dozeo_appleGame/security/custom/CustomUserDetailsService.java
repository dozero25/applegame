package com.project.dozeo_appleGame.security.custom;

import com.project.dozeo_appleGame.entity.User;
import com.project.dozeo_appleGame.repository.account.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = accountRepository.findUserByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new CustomUserDetails(userEntity);
    }
}
