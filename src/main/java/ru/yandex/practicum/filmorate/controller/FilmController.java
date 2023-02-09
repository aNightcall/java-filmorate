package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();
    private static int id = 1;

    @PostMapping("/films")
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        try {
            validateFilm(film);
            film.setId(id);
            films.put(id++, film);
            log.info("Фильм {} успешно добавлен.", film.getName());
        } catch (ValidationException e) {
            log.info("Не получилось добавить фильм {}. {}", film.getName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(film);
        }
        return ResponseEntity.status(HttpStatus.OK).body(film);
    }

    @PutMapping("/films")
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        try {
            validateFilm(film);
            if (films.containsKey(film.getId())) {
                films.remove(film.getId());
                films.put(film.getId(), film);
                log.info("Фильм {} успешно обновлён.", film.getName());
            } else {
                throw new ValidationException("Такой фильм не был добавлен.");
            }
        } catch (ValidationException e) {
            log.info("Не получилось обновить фильм {}. {}", film.getName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(film);
        }
        return ResponseEntity.status(HttpStatus.OK).body(film);
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    public void validateFilm(Film film) throws ValidationException {
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма не может содержать больше 200 символов.");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не может быть раньше первого в истории кинопоказа.");
        } else if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
    }
}
