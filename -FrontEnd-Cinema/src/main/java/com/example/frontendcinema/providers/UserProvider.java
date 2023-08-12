package com.example.frontendcinema.providers;

import com.example.frontendcinema.pojoes.User;
import com.example.frontendcinema.services.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import com.example.frontendcinema.details.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailService userDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        User user = userDetails.getUser();
        if (user == null) throw new BadCredentialsException("Пользователь '"+username+"' не найден");
        if (!password.equals(user.getPassword())) throw new BadCredentialsException("Неверный пароль");
        return new UsernamePasswordAuthenticationToken(user, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
