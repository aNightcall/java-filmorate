package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.HttpURLConnection;
import java.util.*;

@Slf4j
@RestController
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private static int id = 1;

    @PostMapping("/users")
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        try {
            user = validateUser(user);
            user.setId(id);
            users.put(id++, user);
            log.info("Пользователь {} успешно добавлен.", user.getLogin());
        } catch (ValidationException e) {
            log.info("Не получилось добавить пользователя {}. {}", user.getLogin(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/users")
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        try {
            user = validateUser(user);
            if (users.containsKey(user.getId())) {
                users.remove(user.getId());
                users.put(user.getId(), user);
                log.info("Информация о пользователе {} успешно обновлена.", user.getLogin());
            } else {
                throw new ValidationException("Такой пользователь не был добавлен.");
            }
        } catch (ValidationException e) {
            log.info("Не получилось обновить информацию о пользователе {}. {}", user.getLogin(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/users")
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User validateUser(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен содержать пробелы.");
        } else if ("".equals(user.getName()) || user.getName() == null) {
            user.setName(user.getLogin());
        }
        return user;
    }

}
