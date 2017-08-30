<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>WebChat | 聊天</title>
    <jsp:include page="include/commonfile.jsp"/>
    <script src="${ctx}/static/plugins/sockjs/sockjs.js"></script>
</head>
<body>
<jsp:include page="include/header.jsp"/>
<div class="am-cf admin-main">
    <jsp:include page="include/sidebar.jsp"/>
    <!-- content start -->
    <div class="admin-content">
        <div class="" style="width: 80%;float:left;">
            <!-- 聊天区 -->
            <div class="am-scrollable-vertical" id="chat-view" style="height: 510px;">
                <ul class="am-comments-list am-comments-list-flip" id="chat">

                </ul>
            </div>
            <!-- 输入区 -->
            <div class="am-form-group am-form">
                <textarea class="" id="message" name="message" rows="5"  placeholder="这里输入你想发送的信息..."></textarea>
            </div>
            <!-- 接收者 -->
            <div class="" style="float: left">
                <p class="am-kai">发送给 : <span id="sendto">全体成员</span><button class="am-btn am-btn-xs am-btn-danger" onclick="$('#sendto').text('全体成员')">复位</button></p>
            </div>
            <!-- 按钮区 -->
            <div class="am-btn-group am-btn-group-xs" style="float:right;">
                <button class="am-btn am-btn-default" type="button" onclick="getConnection()"><span class="am-icon-plug"></span> 连接</button>
                <button class="am-btn am-btn-default" type="button" onclick="closeConnection()"><span class="am-icon-remove"></span> 断开</button>
                <button class="am-btn am-btn-default" type="button" onclick="checkConnection()"><span class="am-icon-bug"></span> 检查</button>
                <button class="am-btn am-btn-default" type="button" onclick="clearConsole()"><span class="am-icon-trash-o"></span> 清屏</button>
                <button class="am-btn am-btn-default" type="button" onclick="sendMessage()"><span class="am-icon-commenting"></span> 发送</button>
            </div>
        </div>
        <!-- 列表区 -->
        <div class="am-panel am-panel-default" style="float:right;width: 20%;">
            <div class="am-panel-hd">
                <h3 class="am-panel-title">在线列表 [<span id="onlinenum"></span>]</h3>
            </div>
            <%--<ul class="am-list am-list-static am-list-striped" >--%>
                <%--<li>图灵机器人<button class="am-btn am-btn-xs am-btn-danger" id="tuling" data-am-button>未上线</button></li>--%>
            <%--</ul>--%>
            <ul class="am-list am-list-static am-list-striped" id="list">
                <%--在此处动态加入除去自己在线的所有人--%>
            </ul>
        </div>
    </div>
    <!-- content end -->
</div>
<a href="#" class="am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}">
    <span class="am-icon-btn am-icon-th-list"></span>
</a>
<jsp:include page="include/footer.jsp"/>

<script>
    $(function () {

    });
    if("${message}"){
        layer.msg('${message}', {offset: 0});
    }
    if("${error}"){
        layer.msg('${error}', {offset: 0, shift: 6});
    }

    var url=null;
    var ws=null;
    url="ws://" + location.host+"${pageContext.request.contextPath}" + "/chat";
    ws=new WebSocket(url);
    ws.onopen=function (event) {
        layer.msg("连接已经建立",{offset:0});
    };
    ws.onmessage=function (event) {//解析后台发送过来的json字符串
        analysisMessage(event.data);
    };
    ws.onclose=function (event) {
        layer.msg("连接已经关闭",{offset:0});
    };
    ws.onerror=function (event) {
        layer.msg("连接发生错误",{offset:0});
    };

    function getConnection(){//获取链接
        if(ws==null){
            ws=new WebSocket(url);
            ws.onopen=function (event) {
                layer.msg("连接已经建立",{offset:0});
            };
            ws.onmessage=function (event) {
                analysisMessage(event.data);
            };
            ws.onclose=function (event) {
                layer.msg("连接已经关闭",{offset:0});
            };
            ws.onerror=function (event) {
                layer.msg("连接发生错误",{offset:0});
            };
        }else {
            layer.msg("已经建立连接",{offset:0});
        }
    }

    function closeConnection(){//关闭连接
        if(ws!=null){
            ws.close();
            ws=null;//关闭连接
            $("#list").html("");//清空人员在线列表
            layer.msg("连接已经关闭",{offset:0});
        }else {
            layer.msg("连接已经关闭",{offset:0});
        }
    }

    function checkConnection(){//检查连接
        if(ws!=null){
            layer.msg(ws.readyState==0?"连接异常":"连接正常",{offset:0});
        }else {
            layer.msg("连接没有开启",{offset:0});
        }
    }

    function sendMessage(){//发送信息给后台
        if(ws==null){
            layer.msg("连接没有开启",{offset:0});
        }
        var message=$("#message").val();
        var to=$("#sendto").text() == "全体成员"? "": $("#sendto").text();
        if(message==null||message==''){
            layer.msg("你还没有输入内容",{offset:0});
            return;
        }
        var msg={//组装json信息格式
            message:{
                content:message,
                from:'${user.userid}',
                to:to,
                time:getDateFull()
            },
            type:"message"
        };
        ws.send(JSON.stringify(msg));//变成字符串发送过去
    }

    /**
     * 解析后台传来的消息
     * "message":{"from":"xxx","to":"xxx","content":"xxx","time":"xxxx.xx.xx"}
     * "type":{notice|message}
     * "list":["tom","admin"]
     */
    function analysisMessage(message){
        message = JSON.parse(message);
        if(message.type == "message"){//会话消息
            showChat(message.message);
        }
        if(message.type == "notice"){//提示消息
            showNotice(message.message);
        }
        if(message.list != null && message.list != undefined){//在线列表
            showOnline(message.list);
        }
    }

    function showChat(message){//展示会话信息
        var to = message.to == null || message.to == ""? "全体成员" : message.to;//获取接收人
        var isSef = '${userid}' == message.from ? "am-comment-flip" : "";//如果是自己则显示在右边,他人信息显示在左边
        var html =
            "<li class=\"am-comment "+isSef+"am-comment-primary\">" +
            "<a href=\"#link-to-user-home\"><img width=\"48\" height=\"48\" class=\"am-comment-avatar\" alt=\"\" src=\"${ctx}/"+message.from+"/head\">" +
            "</a>" +
            "<div class=\"am-comment-main\">\n"+"<header class=\"am-comment-hd\">" +
                "<div class=\"am-comment-meta\"><a class=\"am-comment-author\" href=\"#link-to-user\">"+message.from+"</a> " +
                    "发表于<time> "+message.time+"</time> 发送给: "+to+" " +
                "</div></header><div class=\"am-comment-bd\"><p>"+message.content+"</p></div>" +
            "</div>" +
            "</li>";
        $("#chat").append(html);
        $("#message").val("");//消息发送出去之后清空输入区
        var chat = $("#chat-view");//让聊天区始终滚动到最下面
        chat.scrollTop(chat[0].scrollHeight);
    }

    function showNotice(notice){//展示提示信息
        $("#chat").append("<div><p class=\"am-text-success\" style=\"text-align:center\"><span class=\"am-icon-bell\"></span> "+notice+"</p></div>");
        var chat = $("#chat-view");
        chat.scrollTop(chat[0].scrollHeight);
    }

    function showOnline(list){//展示在线列表
        $("#list").html("");//清空列表,重新加载
        $.each(list,function (index,item) {
            var li = "<li>"+item+"</li>";//添加自己
            if('${user.userid}'!=item){//排除自己
                li = "<li>"+item+"<button type=\"button\" class=\"am-btn am-btn-xs am-btn-primary am-round\" " +
                     "onclick=\"addChat('"+item+"');\"><span class=\"am-icon-phone\"><span>私聊</button></li>";
            }
            $("#list").append(li);
        });
        $("#onlinenum").text($("#list li").length);//显示当前共在线多少人
    }

    function addChat(user){//添加接收人并排除重复
        var sendto = $("#sendto");
        var receive = sendto.text() == "全体成员"? "":sendto.text()+",";
        if(receive.indexOf(user) == -1){//排除重复
            sendto.text(receive + user);
        }
    }

    function clearConsole(){//清除聊天窗口
        $("#chat").html("");
    }

    function appendZero(s){return ("00"+ s).substr((s+"").length);}//补0函数

    function getDateFull(){
        var date = new Date();
        var currentdate = date.getFullYear() + "-" + appendZero(date.getMonth() + 1) + "-" + appendZero(date.getDate()) + " " + appendZero(date.getHours()) + ":" + appendZero(date.getMinutes()) + ":" + appendZero(date.getSeconds());
        return currentdate;
    }
</script>
</body>
</html>
