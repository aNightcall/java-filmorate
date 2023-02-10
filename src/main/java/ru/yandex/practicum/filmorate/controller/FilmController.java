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
@RequestMapping("/films")
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();
    private static int id = 1;

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        film.setId(id);
        films.put(id++, film);
        log.info("Фильм {} успешно добавлен.", film.getName());
        return ResponseEntity.status(HttpStatus.OK).body(film);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
            films.put(film.getId(), film);
            log.info("Фильм {} успешно обновлён.", film.getName());
        } else {
            throw new ValidationException("Такой фильм не был добавлен.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(film);
    }

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

}
