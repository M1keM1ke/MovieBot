package ru.mike.moviebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mike.moviebot.domain.genre.BaseGenre;

import java.util.Optional;
import java.util.UUID;

public interface BaseGenreRepo extends JpaRepository<BaseGenre, UUID> {

    Optional<BaseGenre> findByGenreName(String genreName);

    Optional<BaseGenre> findByGenreValue(Integer genreValue);
}
