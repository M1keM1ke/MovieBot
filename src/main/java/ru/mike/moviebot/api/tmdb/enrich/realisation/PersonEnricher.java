package ru.mike.moviebot.api.tmdb.enrich.realisation;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.BasePerson;
import com.uwetrottmann.tmdb2.entities.PersonResultsPage;
import com.uwetrottmann.tmdb2.services.SearchService;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import ru.mike.moviebot.api.tmdb.enrich.TmdbMessageEnricher;

import java.io.IOException;
import java.util.Optional;

@Component
public class PersonEnricher implements TmdbMessageEnricher {
    Tmdb tmdb = new Tmdb(System.getenv("TMDB_TOKEN"));
    SearchService searchService = tmdb.searchService();

    @Override
    public Optional<BasePerson> enrichPerson(String name) {
        Response<PersonResultsPage> response;
        try {
            response =
                    searchService.person(name, 1, "ru", null, false).execute();

            if (response.isSuccessful() && response.body().results.size() != 0) {
                return Optional.of(response.body().results.get(0));
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();

        }
        return Optional.empty();
    }
}
