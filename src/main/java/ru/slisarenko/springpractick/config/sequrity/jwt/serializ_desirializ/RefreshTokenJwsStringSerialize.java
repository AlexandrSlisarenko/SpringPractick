package ru.slisarenko.springpractick.config.sequrity.jwt.serializ_desirializ;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.slisarenko.springpractick.config.sequrity.jwt.dto.Token;

import java.util.Date;
import java.util.function.Function;

@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
public class RefreshTokenJwsStringSerialize implements Function<Token, String> {

    private final JWEEncrypter jweEncrypter;

    @Setter
    private EncryptionMethod encryptionMethod = EncryptionMethod.A192CBC_HS384;

    @Setter
    private JWEAlgorithm jweAlgorithm = JWEAlgorithm.DIR;

    @Override
    public String apply(Token token) {
        var jwtHeaders = new JWEHeader.Builder(this.jweAlgorithm, this.encryptionMethod)
                .keyID(token.id().toString())
                .build();
        var claimJwt = new JWTClaimsSet.Builder()
                .claim("authorities", token.authorities())
                .jwtID(token.id().toString())
                .subject(token.username())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .build();

        var encryptedJWT = new EncryptedJWT(jwtHeaders, claimJwt);

        try {
            encryptedJWT.encrypt(this.jweEncrypter);
            return encryptedJWT.serialize();
        } catch (JOSEException e){
            log.error(e.getMessage(), e);
        }

        return "";
    }
}
