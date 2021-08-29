package ru.mike.moviebot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "pos", "row", "col", "len", "word", "s"})
public class Actor {
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("pos")
    private Integer pos;
    @JsonProperty("row")
    private Integer row;
    @JsonProperty("col")
    private Integer col;
    @JsonProperty("len")
    private Integer len;
    @JsonProperty("word")
    private String word;
    @JsonProperty("s")
    private List<String> s;
}
