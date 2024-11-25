package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.service.UserService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class IndexControllerTest {

    IndexController indexController;

    @BeforeEach
    public void initServices() {
        indexController = new IndexController();
    }

    @Test
    void getIndex() {
        var view = indexController.getIndex();
        assertThat(view).isEqualTo("index");
    }
}