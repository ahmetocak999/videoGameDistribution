package com.example.gameDistribution.Service;

import com.example.gameDistribution.Entity.AuthEntity;
import com.example.gameDistribution.Repository.AuthRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AuthRepo authRepo;

    // loadUserByUsername: kullanıcıyı veritabanından alır ve UserDetails döner
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authRepo.findByUsername(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }


    private static class CustomUserDetails implements UserDetails {
        private final AuthEntity authEntity;

        public CustomUserDetails(AuthEntity authEntity) {
            this.authEntity = authEntity;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {



            return Collections.singletonList(new SimpleGrantedAuthority(authEntity.getRole()));
        }

        @Override
        public String getPassword() {
            return authEntity.getPassword();
        }


        @Override
        public String getUsername() {
            return authEntity.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}