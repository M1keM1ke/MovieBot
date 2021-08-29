package ru.mike.moviebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.mike.moviebot.domain.list.MovieList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieListRepository extends JpaRepository<MovieList, UUID> {

    Optional<MovieList> findByListCallbackData(@Param("listCallbackData") String listCallbackData);

    Optional<MovieList> deleteByTmdbListId(Integer tmdbListId);

    @Query("select ml from MovieList ml where ml.user.chatId = :chatId and ml.wasListSelected = :wasSel")
    List<MovieList> findByChatIdAndWasListSelected(
            @Param("chatId") Long userBotId, @Param("wasSel") Boolean wasListSelected);

    List<MovieList> findAllByUserChatId(Long chatId);
}