package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private static int id = 1;

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        user = checkName(user);
        user.setId(id);
        users.put(id++, user);
        log.info("Пользователь {} успешно добавлен.", user.getLogin());
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        user = checkName(user);
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            users.put(user.getId(), user);
            log.info("Информация о пользователе {} успешно обновлена.", user.getLogin());
        } else {
            throw new ValidationException("Такой пользователь не был добавлен.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User checkName(User user) {
        if ("".equals(user.getName()) || user.getName() == null) {
            user.setName(user.getLogin());
        }
        return user;
    }

}
