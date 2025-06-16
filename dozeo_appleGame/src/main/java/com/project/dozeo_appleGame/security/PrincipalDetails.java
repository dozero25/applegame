package com.project.dozeo_appleGame.security;

import com.project.dozeo_appleGame.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails{

    @Getter
    private final User user;

    @Override
    public String getPassword() {
        String pw = "";
        if(user != null){
            pw = user.getPassword();
        }
        return pw;
    }

    @Override
    public String getUsername() {
        String username = "";
        if(user != null){
            username = user.getUsername();
        }
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
