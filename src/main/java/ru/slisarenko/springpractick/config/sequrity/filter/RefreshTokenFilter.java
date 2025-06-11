package ru.slisarenko.springpractick.config.sequrity.filter;

import com.fasterxml.jackson.core.filter.TokenFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.slisarenko.springpractick.config.sequrity.jwt.dto.InformationUsefulFromToken;
import ru.slisarenko.springpractick.config.sequrity.jwt.dto.Token;
import ru.slisarenko.springpractick.dto.TokenUser;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.function.Function;

@Setter
public class RefreshTokenFilter extends OncePerRequestFilter {

    private RequestMatcher requestMatcher;

    private SecurityContextRepository securityContextRepository;

    private Function<Authentication, Token> refreshTokenFactory;

    private Function<Token, Token> accessTokenFactory;

    private Function<Token, String> refreshTokenStringSerializer;

    private Function<Token, String> accessTokenStringSerializer;

    private ObjectMapper objectMapper = new ObjectMapper();


    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (this.requestMatcher.matches(request)) {
            if (this.securityContextRepository.containsContext(request)) {
                var context = this.securityContextRepository.loadDeferredContext(request).get();
                if (context != null && context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken &&
                    context.getAuthentication().getPrincipal() instanceof TokenUser user &&
                    context.getAuthentication().getAuthorities()
                            .contains(new SimpleGrantedAuthority("JWT_REFRESH"))) {
                    var accessToken = this.accessTokenFactory.apply(user.getToken());

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    this.objectMapper.writeValue(response.getWriter(),
                            new InformationUsefulFromToken(this.accessTokenStringSerializer.apply(accessToken),
                                    accessToken.expiresAt().toString(), null, null));
                    return;
                }
            }

            throw new AccessDeniedException("User must be authenticated with JWT");
        }

        filterChain.doFilter(request, response);

    }
}
