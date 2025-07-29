package com.malise.trading.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class Home {
    @GetMapping("/home")
    public String index() {
        return "Welcome to Malise Trading!";
    }
}
