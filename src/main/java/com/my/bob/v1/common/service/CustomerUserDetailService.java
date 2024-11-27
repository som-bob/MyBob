package com.my.bob.v1.common.service;

import com.my.bob.v1.member.entity.BobUser;
import com.my.bob.v1.member.service.BobUserService;
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
    // 현재는 로그인시에 BobUseService 사용한다.
    // 또한 email을 통한 유저 정보 조회 후, 이메일, 권한 정도만 세팅해서 UserDetails 값으로 전해주기 때문에, 현재로서 사용처는 없다.
    // 만약에 CustomerUserDetail을 만들어 이메일, 권한, 사진 같은 정보를 더 세팅해 줘야 할 경우, 해당 서비스를 LoginService에서 사용하도록 한다.

    private final BobUserService bobUserService;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        if(! bobUserService.existByEmail(userEmail)) {
            throw new UsernameNotFoundException("존재하지 않는 회원입니다.");
        }

        BobUser user = bobUserService.getByEmail(userEmail);
        String authority = user.getAuthority();
        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(authority));

        return new User(userEmail, user.getPassword(), authorities);
    }
}
