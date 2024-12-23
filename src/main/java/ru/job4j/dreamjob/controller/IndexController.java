package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.dreamjob.model.User;

import javax.servlet.http.HttpSession;

@Controller
@ThreadSafe
public class IndexController {

    @GetMapping({"/", "/index"})
    public String getIndex() {
        return "index";
    }

}
