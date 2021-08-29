package ru.mike.moviebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.mike.moviebot.domain.param.ParamMovie;

import java.util.Optional;
import java.util.UUID;

public interface ParamMovieRepo extends JpaRepository<ParamMovie, UUID> {

    @Query("select p from ParamMovie p where p.user.chatId = :botId")
    Optional<ParamMovie> findParamMovieById(@Param("botId") long userBotId);
}
