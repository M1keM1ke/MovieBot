package ru.mike.moviebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.mike.moviebot.domain.top.YearTop;

import java.util.Optional;
import java.util.UUID;

public interface YearTopRepo extends JpaRepository<YearTop, UUID> {

    @Query("select y from YearTop y where y.user.chatId = :botId")
    Optional<YearTop> findYearTopById(@Param("botId") Long userBotId);
}
