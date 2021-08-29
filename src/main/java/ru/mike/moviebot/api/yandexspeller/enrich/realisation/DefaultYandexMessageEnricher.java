package ru.mike.moviebot.api.yandexspeller.enrich.realisation;

import com.uwetrottmann.tmdb2.entities.BasePerson;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import ru.mike.moviebot.api.yandexspeller.enrich.YandexMessageEnricherAdapter;
import ru.mike.wrapper.YandexSpeller;
import ru.mike.wrapper.dto.CheckTextResult;
import ru.mike.wrapper.service.CheckTextService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DefaultYandexMessageEnricher extends YandexMessageEnricherAdapter {

    YandexSpeller yandexSpeller = new YandexSpeller();
    CheckTextService checkTextService = yandexSpeller.checkTextService();

    @Override
    public BasePerson enrichCheckText(String text, String lang) {
        Response<List<CheckTextResult>> response = null;
        List<CheckTextResult> correctActors = new ArrayList<>();
        BasePerson basePerson = new BasePerson();

        try {
            List<String> userWords = checkUserText(text);

            if (userWords != null) {
                response = checkTextService.checkText(text, lang, 4, null, null).execute();
                if (response.isSuccessful()) {
                    correctActors = response.body();

                }

                basePerson.name = createPersonName(correctActors, userWords);
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return basePerson;
    }

    private ArrayList<String> checkUserText(String userText) {

        String[] wordsArray = userText.trim().replaceAll("[*%?:]", "").split(" ");
        return wordsArray.length == 2 ? new ArrayList<>(Arrays.asList(wordsArray)) : null;
    }

    private String createPersonName(List<CheckTextResult> correctActors, List<String> actors) {
        String actorName;

        switch (correctActors.size()) {
            case 2:
                actorName = correctActors.get(0).getS().get(0) + " " + correctActors.get(1).getS().get(0);
            break;
            case 1:
                if (correctActors.get(0).getWord().equals(actors.get(0))) {
                    actorName = correctActors.get(0).getS().get(0) + " " + actors.get(1);
                } else {
                    actorName = actors.get(0) + " " + correctActors.get(0).getS().get(0);
                }
                break;
            default: actorName = actors.get(0) + " " + actors.get(1); //for 0 length
        }
        return actorName;
    }
}
