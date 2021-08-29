package ru.mike.moviebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.mike.moviebot.domain.top.GenreTop;

import java.util.Optional;
import java.util.UUID;

public interface GenreTopRepo extends JpaRepository<GenreTop, UUID> {

    Optional<GenreTop> findGenreTopByUserChatId(@Param("genreId") Long userBotId);
}
