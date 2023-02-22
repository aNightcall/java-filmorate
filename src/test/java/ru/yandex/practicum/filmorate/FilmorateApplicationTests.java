package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FilmorateApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmorateApplicationTests {

	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void failFilmNameTest() {
		Film film = getFilm();
		film.setName("");

		assertEquals(postFilm(film).getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void failDescriptionTest() {
		String description = "";
		for (int i = 0; i < 201; i++) {
			description = description + "c";
		}
		Film film = getFilm();
		film.setDescription(description);

		assertEquals(postFilm(film).getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void failReleaseDate() {
		Film film = getFilm();
		film.setReleaseDate(LocalDate.of(1642, 8, 22));

		assertEquals(postFilm(film).getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void failDurationTest() {
		Film film = getFilm();
		film.setDuration(-1);

		assertEquals(postFilm(film).getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void noUserNameTest() {
		User user = getUser();
		user.setName("");

		assertEquals(postUser(user).getStatusCode(), HttpStatus.OK);
		assertEquals(postUser(user).getBody().getName(), user.getLogin());
	}

	@Test
	public void failEmailTest() {
		User user = getUser();
		user.setEmail("это-неправильный?эмейл@");

		assertEquals(postUser(user).getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void failLoginTest() {
		User user = getUser();
		user.setLogin("");

		assertEquals(postUser(user).getStatusCode(), HttpStatus.BAD_REQUEST);

		user.setLogin("lo gin");

		assertEquals(postUser(user).getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void failBirthdayTest() {
		User user = getUser();
		user.setBirthday(LocalDate.of(2122, 1, 1));

		assertEquals(postUser(user).getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	public URI getUri(String path) {
		return URI.create("http://localhost:" + port + path);
	}

	public Film getFilm() {
		Film film = Film.builder()
				.name("name")
				.description("description")
				.duration(60)
				.releaseDate(LocalDate.of(2001, 1, 1))
				.build();
		return film;
	}

	public ResponseEntity<Film> postFilm(Film film) {
		return restTemplate.postForEntity(getUri("/films"), film, Film.class);
	}

	public User getUser() {
		return User.builder()
				.name("name")
				.login("login")
				.email("mail@yandex.ru")
				.birthday(LocalDate.of(1990, 1, 1))
				.build();
	}

	public ResponseEntity<User> postUser(User user) {
		return restTemplate.postForEntity(getUri("/users"), user, User.class);
	}
}
