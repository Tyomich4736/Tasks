package by.nosevich.internship.task3.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Artyom Nosevich
 * This controller exist only for give you main page
 */
@Controller
public class MainController {
    @GetMapping
    public String getMainPage(){
        return "mainPage";
    }
}
