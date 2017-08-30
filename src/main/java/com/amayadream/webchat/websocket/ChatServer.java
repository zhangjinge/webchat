package com.amayadream.webchat.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amayadream.webchat.pojo.User;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by dell on 2017/4/29.
 */
@ServerEndpoint(value = "/chat",configurator = HttpSessionConfigurator.class)
public class ChatServer {
    //设计成非静态的变量是多个ChatServer对象各自都有的
    private String userid;//当前会话的userid
    private Session session;//当前的会话
    private HttpSession httpSession;
    //ChatServer是多例的,设计成静态的变量是多个ChatServer对象共有的
    private static int onlineNum=0;//当前总共的在线人数
    private static CopyOnWriteArraySet<ChatServer> chatServers=new CopyOnWriteArraySet<>();//放所有的
    private static List<String> list=new ArrayList<>();//放所有的userid
    private static Map<String,Session> bindSession=new HashMap<>();//userid作键,Session作值

    /**
     * 连接建立,onlineNum增加1,将this(ChatServer)对象存放到集合之中
     * userid增加到list集合之中,将this.userid和this.session绑定放到map
     * 将this.userid,this.session,this.httpSession赋值
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig){
        System.out.println("onOpen正在初始化...");
        Map<String, Object> userProperties = endpointConfig.getUserProperties();
        this.httpSession=(HttpSession) userProperties.get(HttpSession.class.getName());//httpSession
        User user =(User) this.httpSession.getAttribute("user");
        this.userid=user.getUserid();//this.userid
        ChatServer.addOnlineNum();//onlineNum
        chatServers.add(this);//this,ChatServer
        list.add(this.userid);//this.userid
        this.session=session;
        bindSession.put(this.userid,session);//this.session
        String message = createMessage("["+userid+"]加入聊天室,当前在线人数为"+getOnlineNum()+"位","notice",list);
        broadcast(message);
        System.out.println("onOpen初始化完成!");
    }

    @OnMessage
    public void onMessage(String message){
        System.out.println("==============================================>onMessage");
        JSONObject obt = JSON.parseObject(message);
        String msg = obt.getString("message");
        JSONObject _msg = JSON.parseObject(msg);
        System.out.println(_msg);
        if(_msg.getString("to")==null||"".equals(_msg.getString("to"))){//群发
            broadcast(message);
        }else {//私聊
            String[] userIds = _msg.getString("to").split(",");
            p2p(message,bindSession.get(_msg.getString("from")));//消息先发给自己
            for (String userid:userIds){
                if(!userid.equals(_msg.getString("from"))){
                    String _userid = _msg.getString(userid);
                    Session session = bindSession.get(_userid);
                    p2p(message,session);
                }
            }
        }
    }

//    var msg={//组装json信息格式
//        message:{
//            content:message,
//            from:'${user.userid}',
//            to:to,
//            time:getDateFull()
//        },
//        type:"message"
//    };
    public void p2p(String message, Session session){//私聊
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(){
        System.out.println("onClose正在执行...");
        chatServers.remove(this);//将当前ChatServer对象移除
        ChatServer.subOnlineNum();//在线人数减一
        list.remove(userid);//从ID列表中移除
        bindSession.remove(userid);
        String message = createMessage("["+userid+"]离开了聊天室,当前在线人数为"+getOnlineNum()+"位","notice",list);
        broadcast(message);
        System.out.println("onClose执行完成!");
    }

    @OnError
    public void onError(Throwable error){
        error.printStackTrace();
    }

    /**
     * 该消息发送给所有的用户
     */
    public void broadcast(String message){
        for (ChatServer chatServer:chatServers){
            try {
                chatServer.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String createMessage(String message,String type,List list){//组装数据格式
        JSONObject msg=new JSONObject();
        msg.put("message",message);
        msg.put("type",type);
        msg.put("list",list);
        String str = msg.toJSONString();
        System.out.println(str);
        return str;
    }

    public static int getOnlineNum(){
        return ChatServer.onlineNum;
    }

    public static void subOnlineNum(){
        ChatServer.onlineNum--;
    }

    public static void addOnlineNum(){
        ChatServer.onlineNum++;
    }
}
