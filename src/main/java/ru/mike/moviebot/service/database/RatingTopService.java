package ru.mike.moviebot.service.database;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.domain.top.RatingTop;
import ru.mike.moviebot.exception.EntityNotFoundException;
import ru.mike.moviebot.repository.RatingTopRepo;
import ru.mike.moviebot.service.common.TelegramService;

@Service
@Slf4j
public class RatingTopService {
    @Autowired
    RatingTopRepo ratingTopRepo;

    public RatingTop findRatingTopByChatId(Long chatId) throws EntityNotFoundException {
        return ratingTopRepo.findRatingTopById(chatId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "RatingTop entity with chatId=" + chatId + "not found!"
                ));
    }

    public boolean disableRatingEntity(Update update) {
        RatingTop ratingTop = findRatingTopByChatId(TelegramService.getMessage(update).getChatId());

        ratingTop.setPage(1);
        ratingTop.setMovieNum(0);

        ratingTopRepo.save(ratingTop);
        log.info(String.format("Disable %s entity was successful", RatingTop.class.getName()));
        return true;
    }

    public void saveRatingTop(@NonNull RatingTop ratingTop) {
        ratingTopRepo.save(ratingTop);
        log.info("RatingTop entity with id=" + ratingTop.getId() + " saved.");
    }
}
