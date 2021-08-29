package ru.mike.moviebot.service.database;

import com.uwetrottmann.tmdb2.entities.List;
import com.uwetrottmann.tmdb2.entities.ListCreateRequest;
import com.uwetrottmann.tmdb2.entities.ListCreateResponse;
import com.uwetrottmann.tmdb2.entities.Status;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mike.moviebot.api.tmdb.enrich.realisation.MovieListEnricher;
import ru.mike.moviebot.domain.User;
import ru.mike.moviebot.domain.list.MovieList;
import ru.mike.moviebot.dto.MovieOfListDto;
import ru.mike.moviebot.exception.EntityNotFoundException;
import ru.mike.moviebot.mapper.MovieMapper;
import ru.mike.moviebot.repository.MovieListRepository;
import ru.mike.moviebot.service.common.TelegramService;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class MovieListService {
    @Autowired
    private MovieListRepository movieListRepository;
    @Autowired
    private MovieListEnricher movieListEnricher;
    @Autowired
    UserService userService;
    @Autowired
    MovieMapper movieMapper;

    public void saveMovieList(@NonNull MovieList movieList) {
        movieListRepository.save(movieList);
        log.info("MovieList entity with id=" + movieList.getId() + " saved.");
    }

    public MovieList findMovieListByListCallbackData(String listCallback) {
        return movieListRepository.findByListCallbackData(listCallback)
                .orElseThrow(() -> new EntityNotFoundException(
                        "MovieList entity with callback id=" + listCallback + "not found!"
                ));
    }

    public Optional<MovieList> findMovieListByListCallbackDataForHandlerCheck(String listCallback) {
        return movieListRepository.findByListCallbackData(listCallback);
    }

    @Transactional
    public Boolean deleteMovieListInDatabaseByListId(Integer tmdbListId) {
        if (movieListRepository.deleteByTmdbListId(tmdbListId).isPresent()) {
            log.info("movie list:" + tmdbListId + " deleted");
            return true;
        }
        log.warn("can not delete list:" + tmdbListId + " in database");
        return false;
    }

    public Optional<Status> clearMovieListById(String sessionId, Integer listId) {
        return movieListEnricher.clearList(sessionId, listId);
    }

    public Boolean deleteMovieListInDbAndTmdb(String sessionId, Integer listId) {
        if (movieListEnricher.deleteList(sessionId, listId)) {
            deleteMovieListInDatabaseByListId(listId);
            return true;
        }
        log.warn("can not delete list:" + listId + " in tmdb");
        return false;
    }

    public MovieList createNewMovieList(Update update, String sessionId, ListCreateRequest listCreateRequest) {
        ListCreateResponse listCreateResponse = movieListEnricher.createList(sessionId, listCreateRequest).get();
        User userByChatId = userService.findUserByChatId(TelegramService.getMessage(update).getChatId());
        MovieList movieList = buildMovieList(userByChatId, listCreateResponse.list_id, listCreateRequest);
        return movieList;
    }

    public ListCreateRequest createListRequest(String name, String description, String language) {
        ListCreateRequest listCreateRequest = new ListCreateRequest();
        listCreateRequest.name = name;
        listCreateRequest.description = description;
        listCreateRequest.language = language;
        return listCreateRequest;
    }

    /**
     * Добавляет фильм в список и возвращает статус
     * такой фильм уже есть в списке = 0;
     * фильм добавлен в список = 1;
     * фильм не добавлен в список = -1;
     *
     * @param tmdbListId - id списка фильмов в TMDB
     * @param mediaId    - id фильма
     * @return статус добавления
     */
    public Integer addMovieToList(String sessionId, Integer tmdbListId, Integer mediaId) {
        return movieListEnricher.addMovieToList(sessionId, tmdbListId, mediaId);
    }

    public java.util.List<MovieList> findMovieListsByChatIdandWasSelected(Long chatId, Boolean wasSelected) {
        java.util.List<MovieList> movieLists = movieListRepository.findByChatIdAndWasListSelected(chatId, wasSelected);
        return movieLists;
    }

    private MovieList buildMovieList(User user, Integer listId, ListCreateRequest listCreateRequest) {
        return MovieList.builder()
                .name(listCreateRequest.name)
                .description(listCreateRequest.description)
                .language(listCreateRequest.language)
                .listCallbackData(UUID.randomUUID().toString())
                .user(user)
                .tmdbListId(listId)
                .wasListSelected(false)
                .build();
    }

    public java.util.List<MovieOfListDto> getMoviesOfList(Integer tmdbListId) {
        Optional<List> movies = movieListEnricher.findMovies(tmdbListId);
        return movieMapper.convertToMovieOfListDtoList(movies.get().items);
    }

    public void unselectAllMovieLists(Long chatId) {
        java.util.List<MovieList> movieLists = movieListRepository.findAllByUserChatId(chatId);

        movieLists.forEach(movieList -> {
            movieList.setWasListSelected(false);
            saveMovieList(movieList);
        });

        log.info("movie lists unselected on user:" + chatId);
    }
}
