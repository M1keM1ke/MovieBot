package ru.mike.moviebot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaDto {
    protected Integer id;
    protected String title;
    protected String urlPoster;
    protected String description;
    protected Double popularity;
    protected Date releaseDate;
}
