package ru.mike.moviebot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import ru.mike.moviebot.domain.list.MovieList;
import ru.mike.moviebot.domain.param.ParamMovie;
import ru.mike.moviebot.domain.top.GenreTop;
import ru.mike.moviebot.domain.top.RatingTop;
import ru.mike.moviebot.domain.top.YearTop;
import ru.mike.moviebot.statemachine.state.BotState;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user_bot")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private UUID id;

    @Column(name = "chat_id")
    private long chatId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_name")
    private String userName;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_state")
    private BotState userState;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "session_token")
    private String sessionToken;

    @Column(name = "current_movie_id")
    private Integer currentMovieId;

    @Column(name = "current_chosen_movie_list")
    private String currentChosenMovieList;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private GenreTop genreTop;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private RatingTop ratingTop;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private YearTop yearTop;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ParamMovie paramMovie;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<MovieList> movieList;
}
