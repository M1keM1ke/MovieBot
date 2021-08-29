package ru.mike.moviebot.service.common;

import com.uwetrottmann.tmdb2.entities.RequestToken;
import com.uwetrottmann.tmdb2.entities.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mike.moviebot.api.tmdb.enrich.realisation.AuthenticationTokenEnricher;

import java.util.Optional;

@Service
@Slf4j
public class TmdbAuthService {
    @Autowired
    private AuthenticationTokenEnricher authenticationTokenEnricher;

    public Session createUserSession(String validatedToken) {
        return authenticationTokenEnricher.createSession(validatedToken).get();
    }

    public String createValidatedTmdbSessionToken() {
        Optional<RequestToken> tmdbToken = authenticationTokenEnricher.createTmdbToken();
        authenticationTokenEnricher.validateToken(tmdbToken.get().request_token);
        return tmdbToken.get().request_token;
    }
}
