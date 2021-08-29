package ru.mike.moviebot.domain.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import ru.mike.moviebot.domain.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "param_movie")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParamMovie {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private UUID id;

    @Column(name = "genre_id")
    private Integer genreId;

    @Column(name = "year")
    private Integer year;

    @Column(name = "popularity")
    private Float popularity;

    @Column(name = "actor")
    private String actor;

    @Column(name = "actor_id")
    private Integer actorId;

    @Column(name = "movie_num")
    private Integer movieNum;

    @Column(name = "page")
    private Integer page;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_bot_id", nullable = false)
    private User user;
}