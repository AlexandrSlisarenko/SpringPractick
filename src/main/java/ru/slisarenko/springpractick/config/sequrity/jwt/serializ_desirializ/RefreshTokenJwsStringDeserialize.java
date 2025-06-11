package ru.slisarenko.springpractick.config.sequrity.jwt.serializ_desirializ;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.slisarenko.springpractick.config.sequrity.jwt.dto.Token;

import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class RefreshTokenJwsStringDeserialize implements Function<String, Token> {

    private final JWEDecrypter decrypter;

    @Override
    public Token apply(String stringToken) {
        try{
            var encryptedJWT = EncryptedJWT.parse(stringToken);
            encryptedJWT.decrypt(this.decrypter);
            var claims = encryptedJWT.getJWTClaimsSet();
            return new Token(
                    UUID.fromString(claims.getJWTID()),
                    claims.getSubject(),
                    claims.getStringListClaim("authorities"),
                    claims.getIssueTime().toInstant(),
                    claims.getExpirationTime().toInstant()
            );
        }catch (ParseException | JOSEException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }
}
