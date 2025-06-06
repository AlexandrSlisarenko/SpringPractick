package ru.slisarenko.springpractick.config.sequrity.jwt.serializ_desirializ;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.slisarenko.springpractick.config.sequrity.jwt.dto.Token;

import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class AccessTokenJwsStingDeserialize implements Function<String, Token> {

    private final JWSVerifier verifier;

    @Override
    public Token apply(String stringToken) {
        try {
            var signedJWT = SignedJWT.parse(stringToken);
            if (signedJWT.verify(this.verifier)) {
                var claims = signedJWT.getJWTClaimsSet();
                var token = new Token(
                        UUID.fromString(claims.getJWTID()),
                        claims.getSubject(),
                        claims.getStringListClaim("authorities"),
                        claims.getIssueTime().toInstant(),
                        claims.getExpirationTime().toInstant()
                );
            }
        } catch (ParseException | JOSEException exception) {
            log.error(exception.getMessage(), exception);
        }

        return null;
    }
}
