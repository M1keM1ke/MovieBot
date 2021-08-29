package ru.mike.moviebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mike.moviebot.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {

    Optional<User> findByChatId(Long chatId);

    Boolean existsByChatId(Long chatId);

}
