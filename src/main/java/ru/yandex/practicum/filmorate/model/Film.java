package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.IsAfter;
import ru.yandex.practicum.filmorate.validator.NotLongerThan;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;

    @IsAfter("1895-12-28")
    private LocalDate releaseDate;

    @NotLongerThan(200)
    private String description;

    @NotNull @NotBlank
    private String name;

    @Positive
    private int duration;
}
