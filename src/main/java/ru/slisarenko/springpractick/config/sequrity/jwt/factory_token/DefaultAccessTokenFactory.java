package ru.slisarenko.springpractick.config.sequrity.jwt.factory_token;

import lombok.Getter;
import lombok.Setter;
import ru.slisarenko.springpractick.config.sequrity.jwt.dto.Token;

import java.time.Duration;
import java.util.function.Function;

import static java.time.Instant.now;

@Getter
public class DefaultAccessTokenFactory implements Function<Token, Token> {

    @Setter
    private Duration tokenTtl = Duration.ofMinutes(20);


    @Override
    public Token apply(Token token) {
        var instant = now();

        var authorities = token.authorities().stream()
                .filter(authority -> authority.startsWith("GRAND_"))
                .map(authority -> authority.substring(authority.indexOf('_') + 1))
                .toList();
        return new Token(token.id(),
                token.username(),
                authorities,
                instant,
                instant.plus(tokenTtl)
                );
    }
}
