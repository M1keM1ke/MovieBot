package ru.mike.moviebot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Movie extends MediaDto{
    public Movie(Integer id, String title, String urlPoster, String description, Double popularity, Date releaseDate) {
        super(id, title, urlPoster, description, popularity, releaseDate);
    }

    @Override
    public String toString() {
        return
                "-Название: " + title + '\n' +
                "-Описание: " + description + '\n' +
                "-TMDB: " + popularity + '\n' +
                "-релиз: " + releaseDate;
    }
}
