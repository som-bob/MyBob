package com.my.bob.member.service;

import com.my.bob.member.entity.BobUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailService implements UserDetailsService {

    private final BobUserService bobUserService;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        if(! bobUserService.existByEmail(userEmail)) {
            throw new UsernameNotFoundException("존재하지 않는 회원입니다.");
        }

        BobUser user = bobUserService.getByEmail(userEmail);
        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(user.getAuthority().name()));

        return new User(userEmail, user.getPassword(), authorities);
    }
}
