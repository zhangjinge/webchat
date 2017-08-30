package com.amayadream.webchat.serviceImpl;

import com.amayadream.webchat.pojo.User;
import com.amayadream.webchat.service.LoginService;
import org.springframework.stereotype.Service;

/**
 * Created by dell on 2017/5/2.
 */
@Service
public class LoginServiceImpl implements LoginService{

    @Override
    public User login(String userid){
        User user = null;
        try {
            user = new User().selectItem(userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
