package ru.mike.moviebot.api.tmdb.enrich;

import com.uwetrottmann.tmdb2.entities.BasePerson;

import java.util.Optional;

public interface TmdbMessageEnricher {

    Optional<BasePerson> enrichPerson(String name);
}
