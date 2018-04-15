package com.springboot.oauth2.service;

import com.google.common.collect.Sets;
import com.springboot.oauth2.dao.UserMapper;
import com.springboot.oauth2.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("userDetailsService")
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userMapper.loadByUsername(username);
        log.debug("loadByUsername:", user.toString());

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username,
                user.getPassword(),
                user.getEnabled(),
                user.getAccountNonExpired(),
                user.getCredentialsNonExpired(),
                user.getAccountNonLocked(),
                this.obtainGrantedAuthorities(user));
        return userDetails;
    }

    /**
     * 获得登录者所有角色的权限集合.
     */
    private Set<GrantedAuthority> obtainGrantedAuthorities(User user) {
        Set<GrantedAuthority> authSet = Sets.newHashSet();
        authSet.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authSet;
    }
}
