package com.spring.home_solver.security.service;

import com.spring.home_solver.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailImpl implements UserDetails {

    private Integer id;

    private String username;

    private String email;

    private String password;

    private Boolean isBan;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public static UserDetailImpl build(User user) {
        List<GrantedAuthority> authorities =
                List.of((GrantedAuthority) new SimpleGrantedAuthority("ROLE_"+user.getRole().name().toUpperCase()));
        return new UserDetailImpl(user.getId(), user.getUsername(),
                user.getEmail(), user.getPassword(),user.getIsBan(), authorities);
    }

}
