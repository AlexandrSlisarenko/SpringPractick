package ru.slisarenko.springpractick.config.sequrity.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.slisarenko.springpractick.config.sequrity.jwt.dto.InformationUsefulFromToken;
import ru.slisarenko.springpractick.config.sequrity.jwt.dto.Token;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;
import java.util.function.Function;

@Setter
public class RequestJwtTokenFilter extends OncePerRequestFilter {

    private RequestMatcher requestMatcher;

    private SecurityContextRepository securityContextRepository;

    private Function<Authentication, Token> refreshTokenFactory;

    private Function<Token, Token> accessTokenFactory;

    private Function<Token, String> refreshTokenStringSerializer;

    private Function<Token, String> accessTokenStringSerializer;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (this.requestMatcher.matches(request)) {
            if (this.securityContextRepository.containsContext(request)) {
                var context = this.securityContextRepository.loadDeferredContext(request).get();
                if (Objects.nonNull(context) && !(context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken)) {
                    var refreshToken = this.refreshTokenFactory.apply(context.getAuthentication());
                    var accessToken = this.accessTokenFactory.apply(refreshToken);

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    this.objectMapper.writeValue(response.getWriter(),
                            new InformationUsefulFromToken(
                                    this.accessTokenStringSerializer.apply(accessToken),
                                    accessToken.expiresAt().toString(),
                                    this.refreshTokenStringSerializer.apply(refreshToken),
                                    refreshToken.expiresAt().toString()
                            ));

                    return;
                }
            }

            throw new AccessDeniedException("User mast be authenticated");
        }

        filterChain.doFilter(request, response);
    }
}
