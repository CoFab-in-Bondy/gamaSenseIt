package fr.ummisco.gamasenseit.server.data.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    private static final String[] apiPaths = {"/auth", "/public", "/private"};
    @Value("${gamaSenseIt.cors-url:}")
    private String corsUrl;

    @GetMapping("/")
    public String messages(Model model) {
        model.addAttribute("app", corsUrl); // avoid redirect, make template with attributes
        return "home";
    }

}
