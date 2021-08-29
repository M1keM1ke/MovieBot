package ru.mike.moviebot.api.yandexspeller.enrich;

import com.uwetrottmann.tmdb2.entities.BasePerson;

public interface YandexMessageEnricher {
    BasePerson enrichCheckText(String text);

    BasePerson enrichCheckText(String text, String lang);
}
