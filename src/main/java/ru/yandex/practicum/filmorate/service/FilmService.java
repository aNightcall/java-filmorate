package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Film addLike(Long id, Long userId) {
        Set<Long> likes = filmStorage.getFilmById(id).getLikes();
        User user = userStorage.getUserById(userId);
        likes.add(user.getId());
        filmStorage.getFilmById(id).setLikes(likes);
        return filmStorage.getFilmById(id);
    }

    public Film deleteLike(Long id, Long userId) {
        Film film = filmStorage.getFilmById(id);
        Set<Long> likes = film.getLikes();
        User user = userStorage.getUserById(userId);
        if (!likes.contains(userId)) {
            throw new NotFoundException("лайк");
        }
        likes.remove(user.getId());
        film.setLikes(likes);
        return film;
    }

    public List<Film> getPopularFilms(Integer size) {
        return filmStorage.getAllFilms().stream()
                .sorted(this::compare)
                .limit(size)
                .collect(Collectors.toList());

    }

    public int compare(Film f1, Film f2) {
        return f2.getLikes().size() - f1.getLikes().size();
    }

}
