package org.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/error/{code}")
public class ErrorsController {

    @GetMapping
    public String defaultError(@PathVariable int code,
                           @RequestParam(required = false) String message,
                           @RequestParam(required = false) String description,
                           Model model) {

        model.addAttribute("code", code);
        model.addAttribute("message", message);
        model.addAttribute("description", description);

        return "/errors/defaultError";
    }
}
