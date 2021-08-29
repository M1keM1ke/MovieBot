package ru.mike.moviebot.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "system")
@Data
public class SystemBotPropertyConfig {
    String name;
    String telegramToken;
    String tmdbToken;
}
