package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;
    private LocalDate releaseDate;
    private String description;

    @NotNull @NotBlank
    private String name;

    @Positive
    private int duration;
}
