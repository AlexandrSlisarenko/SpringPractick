package ru.slisarenko.springpractick.config.sequrity.jwt.serializ_desirializ;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
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
public class AccessTokenJwsStringSerialize implements Function<Token, String> {

    private final JWSSigner signer;

    @Setter
    private JWSAlgorithm algorithm = JWSAlgorithm.HS384;

    @Override
    public String apply(Token token) {
        var jwtHeaders = new JWSHeader.Builder(this.algorithm)
                .keyID(token.id().toString())
                .build();
        var claimJwt = new JWTClaimsSet.Builder()
                .claim("authorities", token.authorities())
                .jwtID(token.id().toString())
                .subject(token.username())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .build();

        var signedJWT = new SignedJWT(jwtHeaders, claimJwt);

        try {
            signedJWT.sign(this.signer);
            return signedJWT.serialize();
        } catch (JOSEException e){
            log.error(e.getMessage(), e);
        }

        return "";
    }
}
