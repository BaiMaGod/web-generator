package com.controller;

import com.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @Autowired
    MainService mainService;

    @RequestMapping("/")
    public String main(Model model){


        return "main";
    }


}
