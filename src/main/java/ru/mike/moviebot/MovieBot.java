package ru.mike.moviebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.telegram.telegrambots.ApiContextInitializer;
import ru.mike.moviebot.config.property.BotMessagePropertyConfig;
import ru.mike.moviebot.config.property.CommandsPropertyConfig;
import ru.mike.moviebot.config.property.SystemBotPropertyConfig;

@SpringBootApplication
@EnableConfigurationProperties({
        BotMessagePropertyConfig.class,
        CommandsPropertyConfig.class,
        SystemBotPropertyConfig.class
})
@ConfigurationPropertiesBinding
public class MovieBot {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(MovieBot.class, args);
    }
}
