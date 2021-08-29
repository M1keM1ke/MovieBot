package ru.mike.moviebot.service.database;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.domain.genre.BaseGenre;
import ru.mike.moviebot.domain.top.GenreTop;
import ru.mike.moviebot.exception.EntityNotFoundException;
import ru.mike.moviebot.repository.GenreTopRepo;
import ru.mike.moviebot.service.common.TelegramService;

@Service
@Slf4j
public class GenreTopService {
    @Autowired
    GenreTopRepo genreTopRepo;
    @Autowired
    BaseGenresService baseGenresService;

    public GenreTop findGenreTopByChatId(Long chatId) {
        return genreTopRepo.findGenreTopByUserChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "GenreTop entity with chatId=" + chatId + "not found!"
                ));
    }

    public boolean disableGenreEntity(Update update) {
        GenreTop genreTop  = findGenreTopByChatId(TelegramService.getMessage(update).getChatId());
        genreTop.setPage(1);
        genreTop.setMovieNum(0);
        genreTop.setGenreId(null);
        genreTopRepo.save(genreTop);

        log.info(String.format("Disable %s entity was successful", GenreTop.class.getName()));
        return true;
    }

    public GenreTop enableGenreEntity(Update update) {
        GenreTop genreTop  = findGenreTopByChatId(TelegramService.getMessage(update).getChatId());
        BaseGenre baseGenre = baseGenresService.findBaseGenreByName(update.getCallbackQuery().getData());

        genreTop.setGenreId(baseGenre.getGenreValue());
        genreTopRepo.save(genreTop);

        return genreTop;
    }

    public void saveGenreTop(@NonNull GenreTop genreTop) {
        genreTopRepo.save(genreTop);
        log.info("GenreTop entity with id=" + genreTop.getId() + " saved.");
    }
}
