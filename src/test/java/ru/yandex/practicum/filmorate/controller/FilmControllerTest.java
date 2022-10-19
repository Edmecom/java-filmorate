package ru.yandex.practicum.filmorate.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmControllerTest {

    private Film crateTestFilm() {
        return  new Film(
                "Звёздные войны: Эпизод 1 — Скрытая угроза",
                "фэнтэзи",
                LocalDate.of(1999, 7, 29),
                130
        );
    }

    @Autowired
    private TestRestTemplate template;

    // Неуспешное добавление - отсутствует название фильма.
    @Test
    public void createFilmNewFilmEmptyName() {
        Film film = crateTestFilm();
        film.setName(null);
        HttpEntity<Film> request = new HttpEntity<>(film);
        ResponseEntity<ValidationException> response = template.postForEntity(
                "/films",
                request,
                ValidationException.class
        );
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Неуспешное добавление - описание фильма длинее 200 символов.
    @Test
    public void createFilmNewFilmTooLongDescription() {
        Film film = crateTestFilm();
        film.setDescription("Мирная и процветающая планета Набу. Торговая федерация, не желая платить налоги, " +
                "вступает в прямой конфликт с королевой Амидалой, правящей на планете, что приводит к войне. " +
                "На стороне королевы и республики в ней участвуют два рыцаря-джедая: учитель и ученик, " +
                "Квай-Гон-Джин и Оби-Ван Кеноби. Вглядитесь в ночное небо. Нет, правда, вглядитесь! " +
                "Не в то, которое над городом и которое освещено миллионом фонарей, окон и неоновых реклам. " +
                "В другое. Темное небо. На нем ровным светом мерцают белесые камушки – мириады звезд. " +
                "Далеких, манящих, завораживающих. Глядя на них, странное чувство закрадывается в душу: " +
                "будто тоска по чему-то забытому, неуловимому, но смутно знакомому… А иногда яркая искра " +
                "прорезает темноту свода – звезда упала. Чей-то мир рухнул.");
        HttpEntity<Film> request = new HttpEntity<>(film);
        ResponseEntity<ValidationException> response = template.postForEntity(
                "/films",
                request,
                ValidationException.class
        );
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Неуспешное добавление - неверная дата релиза.
    @Test
    public void createFilmNewDateOfReliseTooOld() {
        Film film = crateTestFilm();
        film.setReleaseDate(LocalDate.of(1799, 7, 29));
        HttpEntity<Film> request = new HttpEntity<>(film);
        ResponseEntity<ValidationException> response = template.postForEntity(
                "/films",
                request,
                ValidationException.class
        );
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // Неуспешное добавление - отрицательная продолжительность фильма.
    @Test
    public void createFilmNewNegativeDuration() {
        Film film = crateTestFilm();
        film.setDuration(-130);
        HttpEntity<Film> request = new HttpEntity<>(film);
        ResponseEntity<ValidationException> response = template.postForEntity(
                "/films",
                request,
                ValidationException.class
        );
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}