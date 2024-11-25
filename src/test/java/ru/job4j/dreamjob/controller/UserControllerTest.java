package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.service.CandidateService;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.UserService;
import ru.job4j.dreamjob.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    private UserService userService;

    private UserController userController;


    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void getRegistrationPage() {
        var view = userController.getRegistrationPage();
        assertThat(view).isEqualTo("users/register");
    }

    @Test
    void registerWhenUserSavedThenRedirectToVacancies() {
        var user = new User(1, "test@mail.com", "test", "password");
        when(userService.save(user)).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(model, user);

        assertThat(view).isEqualTo("redirect:/vacancies");
    }


    @Test
    void registerWhenEmailExistsThenReturnErrorPage() {
        var user = new User(1, "test@mail.com", "test", "password");
        when(userService.save(user)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = userController.register(model, user);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo("Пользователь с такой почтой уже существует");
    }

    @Test
    void getLoginPage() {
        var view = userController.getLoginPage();
        assertThat(view).isEqualTo("users/login");
    }

    @Test
    void loginUserWhenValidCredentialsThenRedirectToVacancies() {
        var user = new User(1, "test@mail.com", "test", "password");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var request = mock(HttpServletRequest.class);
        var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        var view = userController.loginUser(user, model, request);

        verify(session).setAttribute("user", user);
        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    void loginUserWhenInvalidCredentialsThenReturnLoginPageWithError() {
        var user = new User(1, "test@mail.com", "test", "password");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var request = mock(HttpServletRequest.class);

        var view = userController.loginUser(user, model, request);
        var actualError = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualError).isEqualTo("Почта или пароль введены неверно");
    }



    @Test
    void logout() {
        var session = mock(HttpSession.class);

        var view = userController.logout(session);

        verify(session).invalidate();
        assertThat(view).isEqualTo("redirect:/users/login");
    }
}