package ru.mike.moviebot.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot-message")
@Data
public class BotMessagePropertyConfig {
    private String greetingMsg;
    private String topMovieMsg;
    private String paramMovieMsg;
    private String genreTopList;
}
