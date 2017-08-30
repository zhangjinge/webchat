package com.amayadream.webchat.controller;

import com.amayadream.webchat.pojo.User;
import com.amayadream.webchat.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dell on 2017/4/29.
 */
@Controller
@RequestMapping("/user")
@SuppressWarnings("All")
public class UserController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("/login")
    public String login(String userid, String password, HttpServletRequest request){
        System.out.println("userid:"+userid+"----"+"password:"+password);
        User returnUser = loginService.login(userid);
        if(returnUser!=null){
            request.getSession().setAttribute("userid",returnUser.getUserid());
            request.getSession().setAttribute("user",returnUser);
        }
        //无论有没有先都登陆成功
        return "index";
    }

}
