package ru.mike.moviebot.api.tmdb.enrich.realisation;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.RequestToken;
import com.uwetrottmann.tmdb2.entities.Session;
import com.uwetrottmann.tmdb2.services.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class AuthenticationTokenEnricher {
    private Tmdb tmdb = new Tmdb(System.getenv("TMDB_TOKEN"));
    private AuthenticationService authenticationService = tmdb.authenticationService();

    public Optional<Session> createSession(String validatedToken) {
        Response<Session> sessionResponse = null;
        try {
            sessionResponse = authenticationService.createSession(validatedToken).execute();
            log.info("created session:" + sessionResponse.body().session_id + " with token:" + validatedToken);
            tmdb.setSessionId(sessionResponse.body().session_id);
        } catch (IOException e) {
            log.warn("can not create session with token:" + validatedToken);
            e.printStackTrace();
        } catch (NullPointerException e) {
            log.warn("request was sent but the session is null!");
        }
        return Optional.ofNullable(sessionResponse.body());
    }

    public Optional<RequestToken> validateToken(String tmdbApiToken) {
        Response<RequestToken> requestTokenResponse = null;
        try {
            requestTokenResponse = authenticationService
                    .validateToken("M1keM1ke", "MEIZkHg3w0b5EQC6VX3z", tmdbApiToken).execute();
            log.info("validated token:" + tmdbApiToken);
        } catch (IOException e) {
            log.warn("can not validate token:" + tmdbApiToken);
            e.printStackTrace();
        }
        return Optional.ofNullable(requestTokenResponse.body());
    }

    public Optional<RequestToken> createTmdbToken() {
        Response<RequestToken> tokenResponse = null;
        try {
            tokenResponse = authenticationService.requestToken().execute();
            log.info("created token:" + tokenResponse.body().request_token);
        } catch (IOException e) {
            log.warn("can not create token!");
            e.printStackTrace();
        } catch (NullPointerException e) {
            log.warn("request was sent but the token is null!");
        }
        return Optional.ofNullable(tokenResponse.body());
    }
}
