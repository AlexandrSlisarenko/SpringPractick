package ru.slisarenko.springpractick.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import ru.slisarenko.springpractick.config.sequrity.jwt.dto.Token;
import ru.slisarenko.springpractick.dto.TokenUser;

import java.time.Instant;

public class TokenAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    /**
     * @param preAuthenticatedToken The pre-authenticated authentication token
     * @return UserDetails for the given authentication token, never null.
     * @throws UsernameNotFoundException if no user details can be found for the given
     *                                   authentication token
     */
    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken preAuthenticatedToken)
            throws UsernameNotFoundException {
        if (preAuthenticatedToken.getPrincipal() instanceof Token token) {
            return new TokenUser(token.username(),
                    "{bcrypt}$2a$10$DIm.PQJcTINRAXrQ61.3pOnlzE3tBxso/P7nm2bxfn0Mu.KwbEnIW",
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
