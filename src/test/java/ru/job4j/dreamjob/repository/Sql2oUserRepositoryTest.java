package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.*;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.File;
import ru.job4j.dreamjob.model.User;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Properties;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;

    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepository() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepository.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    void whenSaveUserThenUserIsPersisted() {
        Optional<User> user = sql2oUserRepository.save(new User(0, "test@example.com", "username", "password"));
        Optional<User> savedUser = sql2oUserRepository.findByEmailAndPassword(user.get().getEmail(), user.get().getPassword());

        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }


    @Test
    public void whenUserAlreadyExists() {
        Optional<User> user1 = sql2oUserRepository.save(new User(0, "test@example.com", "username1", "password1"));
        Optional<User> user2 = sql2oUserRepository.save(new User(0, "test@example.com", "username2", "password2"));

        assertTrue(user1.isPresent());
        assertTrue(user2.isEmpty());
    }


}