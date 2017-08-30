package com.amayadream.webchat.serviceImpl;

import com.amayadream.webchat.pojo.User;
import com.amayadream.webchat.service.UploadService;

/**
 * Created by dell on 2017/5/5.
 */
public class UploadServiceImpl implements UploadService {

    @Override
    public User addHeaderImg(String userid,User user){
        User u=null;
        try {
            user.updateItem(userid,user);
            u = user.selectItem(userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }
}
