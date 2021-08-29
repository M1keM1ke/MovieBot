package ru.mike.moviebot.api.tmdb.enrich;

import com.uwetrottmann.tmdb2.entities.BasePerson;

import java.util.Optional;

public class TmdbMessageEnricherAdapter implements TmdbMessageEnricher{
    @Override
    public Optional<BasePerson> enrichPerson(String name) {
        return Optional.empty();
    }
}
