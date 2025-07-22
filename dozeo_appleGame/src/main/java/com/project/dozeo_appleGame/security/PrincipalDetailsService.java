package com.project.dozeo_appleGame.security;

import com.project.dozeo_appleGame.entity.User;
import com.project.dozeo_appleGame.repository.account.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = accountRepository.findUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("회원정보를 찾을 수 없습니다.");
        }

        return new PrincipalDetails(user);
    }
}
