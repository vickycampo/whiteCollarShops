package com.whiteCollar.shops.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/") // This means URL's start with /demo (after Application path)
public class HomeController
{
    @GetMapping
    public String homeController ()
    {
        return "index";
    }
}
