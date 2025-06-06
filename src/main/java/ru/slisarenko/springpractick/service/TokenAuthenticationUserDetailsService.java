package ru.slisarenko.springpractick.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.slisarenko.springpractick.config.sequrity.jwt.dto.Token;
import ru.slisarenko.springpractick.dto.TokenUser;

import java.time.Instant;

public class TokenAuthenticationUserDetailsService implements AuthenticationUserDetailsService {
    /**
     * @param authenticationToken The pre-authenticated authentication token
     * @return UserDetails for the given authentication token, never null.
     * @throws UsernameNotFoundException if no user details can be found for the given
     *                                   authentication token
     */
    @Override
    public UserDetails loadUserDetails(Authentication authenticationToken) throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof Token token) {
            return new TokenUser(token.username(),
                    "pass123",
                    true,
                    true,
                    token.expiresAt().isAfter(Instant.now()),
                    true,
                    token.authorities().stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList(),
                    token
            );
        }
        throw new UsernameNotFoundException("Principal must be of type Token");
    }
}
