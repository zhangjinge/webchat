package com.amayadream.webchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by dell on 2017/5/3.
 */
@Controller
@RequestMapping("/{userid}")
public class LogController {

    @RequestMapping("/log")
    public ModelAndView log(){

        return null;
    }
}
