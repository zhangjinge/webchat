package com.amayadream.webchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dell on 2017/5/3.
 */
@Controller
@RequestMapping("{userid}")
public class RouteController {

    @RequestMapping("/toLogin")
    public String toLogin(HttpServletRequest request, HttpServletResponse response){
        return "login";
    }

    @RequestMapping("/toIndex")
    public String toIndex(HttpServletRequest request, HttpServletResponse response){
        return "index";
    }

    @RequestMapping("/toUpload")
    public String toUpload(HttpServletRequest request, HttpServletResponse response){
        return "upload";
    }

    @RequestMapping("/toDownload")
    public String toDownload(HttpServletRequest request, HttpServletResponse response){
        return "download";
    }
}
