package ru.mike.moviebot.service.actor;

import com.uwetrottmann.tmdb2.entities.BasePerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mike.moviebot.api.tmdb.enrich.realisation.PersonEnricher;
import ru.mike.moviebot.api.yandexspeller.enrich.realisation.DefaultYandexMessageEnricher;

import java.util.Optional;


@Service
public class PersonService {
    @Autowired
    private PersonEnricher personEnricher;
    @Autowired
    private DefaultYandexMessageEnricher yandexMessageEnricher;
    private static final String LANGUAGE_RU = "ru";

    public Optional<BasePerson> findPersonByName(String name) {
        Optional<BasePerson> actor = personEnricher.enrichPerson(name);

        if (!actor.isPresent()) {
            BasePerson correctActor = yandexMessageEnricher.enrichCheckText(name, LANGUAGE_RU);
            actor = personEnricher.enrichPerson(correctActor.name);
        }
        return actor;
    }
}
