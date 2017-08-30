package com.amayadream.webchat.pojo;

import com.amayadream.webchat.annotation.ColumnName;
import com.amayadream.webchat.annotation.IDName;
import com.amayadream.webchat.annotation.TableName;
import com.amayadream.webchat.dao.BaseDao;
import org.junit.Test;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * NAME   :  WebChat/com.amayadream.webchat.pojo
 * Author :  Amayadream
 * Date   :  2016.01.08 14:06
 * TODO   :
 */
@Repository(value = "user")
@TableName("user")
public class User extends BaseDao<User>{
    @IDName(id = true,value = "userid")
    private String userid;      //用户名
    @ColumnName("password")
    private String password;    //密码
    @ColumnName("nickname")
    private String nickname;    //昵称
    private int sex;            //性别
    private int age;            //年龄
    @ColumnName("profilehead")
    private String profilehead; //头像
    private String profile;     //简介
    private String firsttime;   //注册时间
    private String lasttime;    //最后登录时间
    private int status;      //账号状态(1正常 0禁用)

    public User(){
        super();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFirsttime() {
        return firsttime;
    }

    public void setFirsttime(String firsttime) {
        this.firsttime = firsttime;
    }

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getProfilehead() {
        return profilehead;
    }

    public void setProfilehead(String profilehead) {
        this.profilehead = profilehead;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "User{" +
                "userid='" + userid + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", profilehead='" + profilehead + '\'' +
                ", profile='" + profile + '\'' +
                ", firsttime='" + firsttime + '\'' +
                ", lasttime='" + lasttime + '\'' +
                ", status=" + status +
                '}';
    }

    @Test
    public void test() throws Exception{
        this.setUserid("1");
        this.setPassword("1");
        this.setAge(23);
        this.setNickname("a");
        this.setFirsttime(new Date().toString());
        this.setLasttime(new Date().toString());
        this.setSex(1);
        this.setStatus(1);
        this.setProfile("profile");
        this.setProfilehead("profileHead");
        this.addItem(this);
    }

    @Test
    public void test1() throws Exception{
        List<User> users = listItem();
        System.out.println(users);
    }

    @Test
    public void test2() throws Exception{
        User user = selectItem("1");
        System.out.println(user);
    }

    @Test
    public void test3() throws Exception{
        int i= deleteItem("1");
        System.out.println(i);
    }

    @Test
    public void test4() throws Exception{
        this.setPassword("1");
        this.setNickname("jack");
        int i= updateItem("1",this);
        System.out.println(i);
    }
}
