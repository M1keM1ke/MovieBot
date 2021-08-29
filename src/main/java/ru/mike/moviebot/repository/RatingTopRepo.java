package ru.mike.moviebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.mike.moviebot.domain.top.RatingTop;

import java.util.Optional;
import java.util.UUID;

public interface RatingTopRepo extends JpaRepository<RatingTop, UUID> {

    @Query("select r from RatingTop r where r.user.chatId = :botId")
    Optional<RatingTop> findRatingTopById(@Param("botId") Long userBotId);
}
