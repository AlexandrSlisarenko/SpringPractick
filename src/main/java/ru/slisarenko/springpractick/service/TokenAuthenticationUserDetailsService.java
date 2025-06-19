package ru.slisarenko.springpractick.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import ru.slisarenko.springpractick.config.sequrity.jwt.dto.Token;
import ru.slisarenko.springpractick.db.repositary.security.JdbcTokenLogoutRepository;
import ru.slisarenko.springpractick.dto.TokenUser;

import java.time.Instant;

@RequiredArgsConstructor
public class TokenAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final JdbcTokenLogoutRepository repository;

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
            var userPasswordFromDB = repository.getPassword(token.username());
            var indexStart = userPasswordFromDB.indexOf("}") + 1;
            var userPassword = userPasswordFromDB.substring(indexStart, userPasswordFromDB.length() - 1);
            return new TokenUser(token.username(),
                    userPassword,
                    true,
                    true,
                    !repository.isDeactivatedToken(token.id()) &&
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
