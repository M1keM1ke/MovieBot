package ru.mike.moviebot.api.tmdb.enrich.realisation;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.TmdbHelper;
import com.uwetrottmann.tmdb2.entities.List;
import com.uwetrottmann.tmdb2.entities.ListCreateRequest;
import com.uwetrottmann.tmdb2.entities.ListCreateResponse;
import com.uwetrottmann.tmdb2.entities.ListItemStatus;
import com.uwetrottmann.tmdb2.entities.ListOperation;
import com.uwetrottmann.tmdb2.entities.Status;
import com.uwetrottmann.tmdb2.services.ListsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mike.moviebot.api.tmdb.retrofit.ListHelper;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class MovieListEnricher {
    private Tmdb tmdb = new Tmdb(System.getenv("TMDB_TOKEN"));
    private ListsService listsService = tmdb.listsService();
    private static final boolean CONFIRM_CLEAR = true;
    public static final int MOVIE_ALREADY_EXISTS_IN_LIST = 0;
    public static final int MOVIE_ADDED = 1;
    public static final int MOVIE_NOT_ADDED = -1;

    public Optional<ListCreateResponse> createList(String sessionId, ListCreateRequest listCreateRequest) {
        Response<ListCreateResponse> listResponse = null;
        tmdb.setSessionId(sessionId);
        try {
            listResponse = listsService.createList(listCreateRequest).execute();
            if (listResponse.body().success) {
                log.info("created movie list:" + listResponse.body().list_id);
                return Optional.ofNullable(listResponse.body());
            }
        } catch (IOException e) {
            log.warn("can not create movie list on session:" + sessionId);
            e.printStackTrace();
        } catch (NullPointerException e) {
            log.warn("request was sent but the movie list is null!");
        }
        return Optional.empty();
    }

    public Optional<Status> clearList(String sessionId, Integer id) {
        Response<Status> statusResponse = null;
        tmdb.setSessionId(sessionId);
        try {
            statusResponse = listsService.clear(id, CONFIRM_CLEAR).execute();
            if (statusResponse.isSuccessful()) {
                log.info(statusResponse.body().status_message + " Cleared list:" + id);
                return Optional.ofNullable(statusResponse.body());
            }
        } catch (IOException e) {
            log.warn("can not clear movie list with id:" + id);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Boolean deleteList(String sessionId, Integer listId) {
        tmdb.setSessionId(sessionId);
        try {
            Response<Status> statusResponse = listsService.delete(listId).execute();
            if (statusResponse.isSuccessful()) {
                log.info("list:" + listId + " on session:" + sessionId + " deleted.");
                return true;
            }
        } catch (IOException e) {
            log.warn("can not delete list:" + listId + " on session:" + sessionId);
            e.printStackTrace();
        }
        return false;
    }

    public Integer addMovieToList(String sessionId, Integer tmdbListId, Integer mediaId) {
        tmdb.setSessionId(sessionId);
        ListOperation listOperation = new ListOperation();
        listOperation.media_id = mediaId;

        Optional<ListItemStatus> listItemStatus = checkMovieExistingInList(tmdbListId, mediaId);
        if (listItemStatus.isPresent()) {
            try {
                Response<Status> addMovie = listsService.addMovie(tmdbListId, listOperation).execute();
                if (addMovie.isSuccessful()) {
                    log.info("movie:" + mediaId + " added in list:" + tmdbListId + " successful");
                    return MOVIE_ADDED;
                }
            } catch (IOException e) {
                log.warn("can not add movie:" + mediaId + " in list:" + tmdbListId);
                e.printStackTrace();
                return MOVIE_NOT_ADDED;
            }
        }
        return MOVIE_ALREADY_EXISTS_IN_LIST;
    }

    private Optional<ListItemStatus> checkMovieExistingInList(Integer tmdbListId, Integer mediaId) {
        Response<ListItemStatus> listItemStatusResponse = null;
        try {
            listItemStatusResponse = listsService
                    .itemStatus(tmdbListId, mediaId).execute();
            if (listItemStatusResponse.isSuccessful() && !listItemStatusResponse.body().item_present) {
                log.info("item status of movie:" + mediaId + " in list:" + tmdbListId + " checked");
                return Optional.ofNullable(listItemStatusResponse.body());
            }
        } catch (IOException e) {
            log.warn("can not check item status of movie:" + mediaId + " in list:" + tmdbListId);
            e.printStackTrace();
        } catch (NullPointerException e) {
            log.warn("request was sent, but response is null");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List> findMovies(Integer listId) {
        try {
            //запрос с параметром языка, в tmdb api этого параметра нет
            Response<List> listResponse = getRetrofitForList()
                    .create(ListHelper.class)
                    .summary(listId, "26927e2ed70035f352dbe8a4d7b93eae", "ru").execute();

            if (listResponse.isSuccessful()) {
                log.info("received a list summary of list:" + listId);
                return Optional.ofNullable(listResponse.body());
            }
        } catch (IOException e) {
            log.warn("can not get list summary of list:" + listId);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Retrofit getRetrofitForList() {
        return new Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create(TmdbHelper.getGsonBuilder().create()))
                        .build();
    }
}
