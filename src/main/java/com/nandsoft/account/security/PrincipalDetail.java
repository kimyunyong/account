package com.nandsoft.account.security;

import com.nandsoft.account.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 UserDetails를 시큐리티 세션에 저장한다.
public class PrincipalDetail implements UserDetails {
    private User user;
    public PrincipalDetail(User user){
        this.user = user;
    }
    public User getUser(){
        return this.user;
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
    
    // 계정이 만료되지 않았는지 여부 (ture : 만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    // 계정이 잠겨있는지 여부 (true : 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 만료되었는지 여부 (true : 만료안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부 (true : 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

    // 계정 권한 리턴 (권한이 여러개이면 여러개를 리턴하도록 변경해야 함..)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collectors = new ArrayList<>();

        collectors.add(()->{
            return user.getRole();
        });

       /* collectors.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                *//*return "ROLE_" + user.getRole();*//*
                return user.getRole();
            }
        });*/

        return collectors;
    }
}
