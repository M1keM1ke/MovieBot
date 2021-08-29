package ru.mike.moviebot.service.database;

import com.uwetrottmann.tmdb2.entities.Session;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.domain.param.ParamMovie;
import ru.mike.moviebot.domain.top.GenreTop;
import ru.mike.moviebot.domain.top.RatingTop;
import ru.mike.moviebot.domain.top.YearTop;
import ru.mike.moviebot.exception.EntityNotFoundException;
import ru.mike.moviebot.repository.UserRepo;
import ru.mike.moviebot.service.common.TelegramService;
import ru.mike.moviebot.service.common.TmdbAuthService;
import ru.mike.moviebot.statemachine.state.BotState;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    GenreTopService genreTopService;
    @Autowired
    YearTopService yearTopService;
    @Autowired
    RatingTopService ratingTopService;
    @Autowired
    ParamMovieService paramMovieService;
    @Autowired
    TmdbAuthService tmdbAuthService;
    @Autowired
    MovieListService movieListService;

    public User findUserByChatId(@NonNull Long chatId) {
        return userRepo.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User entity with chatId=" + chatId + "not found!"
                ));
    }

    public boolean existsUserByChatId(Long chatId) {
        return userRepo.existsByChatId(chatId);
    }

    public void saveUser(@NonNull User user) {
        userRepo.save(user);
        log.info("User entity with id=" + user.getChatId() + " saved.");
    }

    public void createNewUser(@NonNull Update update) {
        String token = tmdbAuthService.createValidatedTmdbSessionToken();
        Session session = tmdbAuthService.createUserSession(token);

        User newUser = User.builder()
                .firstName(TelegramService.getMessage(update).getChat().getFirstName())
                .lastName(TelegramService.getMessage(update).getChat().getLastName())
                .userName(TelegramService.getMessage(update).getChat().getUserName())
                .chatId(TelegramService.getMessage(update).getChatId())
                .userState(BotState.NEW)
                .sessionToken(token)
                .sessionId(session.session_id)
                .build();

        saveUser(newUser);

        GenreTop genreTop = GenreTop.builder()
                .user(newUser).page(1).movieNum(0)
                .build();
        genreTop.setUser(newUser);
        genreTopService.saveGenreTop(genreTop);

        RatingTop ratingTop = RatingTop.builder()
                .user(newUser).build();
        ratingTop.setUser(newUser);
        ratingTopService.saveRatingTop(ratingTop);

        YearTop yearTop = YearTop.builder()
                .user(newUser).build();
        yearTop.setUser(newUser);
        yearTopService.saveYearTop(yearTop);

        ParamMovie paramMovie = ParamMovie.builder()
                .user(newUser).page(1).movieNum(0).build();
        paramMovie.setUser(newUser);
        paramMovieService.saveParamMovie(paramMovie);

        log.info("New User with id=" + TelegramService.getMessage(update).getChatId() + " created");
    }

    public void setCurrentMovieIdIntoUser(Long chatId, @NonNull Integer movieId) {
        User userByChatId = findUserByChatId(chatId);
        userByChatId.setCurrentMovieId(movieId);
        saveUser(userByChatId);
    }
}
