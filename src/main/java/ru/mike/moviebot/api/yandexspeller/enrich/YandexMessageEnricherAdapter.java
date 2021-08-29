package ru.mike.moviebot.api.yandexspeller.enrich;

import com.uwetrottmann.tmdb2.entities.BasePerson;

public class YandexMessageEnricherAdapter implements YandexMessageEnricher{
    @Override
    public BasePerson enrichCheckText(String text) {
        return null;
    }

    @Override
    public BasePerson enrichCheckText(String text, String lang) {
        return null;
    }
}
