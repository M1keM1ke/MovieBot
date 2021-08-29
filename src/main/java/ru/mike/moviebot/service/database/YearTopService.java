package ru.mike.moviebot.service.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.domain.top.YearTop;
import ru.mike.moviebot.exception.EntityNotFoundException;
import ru.mike.moviebot.repository.UserRepo;
import ru.mike.moviebot.repository.YearTopRepo;
import ru.mike.moviebot.service.common.TelegramService;

@Service
@Slf4j
public class YearTopService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    YearTopRepo yearTopRepo;

    public YearTop findYearTopByChatId(Long chatId) {
        return yearTopRepo.findYearTopById(chatId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "YearTop entity with chatId=" + chatId + "not found!"
                ));
    }

    public void disableYearEntity(Update update) {
        YearTop yearTop = findYearTopByChatId(TelegramService.getMessage(update).getChatId());

        yearTop.setPage(1);
        yearTop.setMovieNum(0);
        yearTopRepo.save(yearTop);
        log.info(String.format("Disable %s entity was successful", YearTop.class.getName()));
    }

    public void saveYearTop(YearTop yearTop) {
        yearTopRepo.save(yearTop);
        log.info("YearTop entity with id=" + yearTop.getId() + " saved.");
    }
}
