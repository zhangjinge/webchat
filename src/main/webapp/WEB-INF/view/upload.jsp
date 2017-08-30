<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>WebChat | 个人设置</title>
    <jsp:include page="include/commonfile.jsp"/>
</head>
<body>
<jsp:include page="include/header.jsp"/>
<div class="am-cf admin-main">
    <!-- content start -->
    <jsp:include page="include/sidebar.jsp"/>
    <div class="admin-content">
        <div class="am-cf am-padding">
            <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">个人设置</strong> / <small>form</small></div>
        </div>
        <div class="am-tabs am-margin" data-am-tabs>
            <ul class="am-tabs-nav am-nav am-nav-tabs">
                <li class="am-active"><a href="#tab1">头像修改</a></li>
            </ul>
            <div class="am-tabs-bd">
                <div class="am-tab-panel am-fade am-in am-active" id="tab1">
                    <form class="am-form am-form-horizontal" action="${ctx}/servlet/UploadServlet" enctype="multipart/form-data" method="post" onsubmit="return checkFileType();" style="text-align: center;">
                        <div style="text-align: center;margin-bottom: 10px">
                            <%--/webchat/static/headers/b/3/cceb4052-7ef6-4985-87ba-476cab138b21_avtar.png--%>
                            <img class="am-circle" src="${ctx}/${user.profilehead}" width="140" height="140" alt="pic"/>
                        </div>
                        <div class="am-form-group am-form-file">
                            <button type="button" class="am-btn am-btn-secondary am-btn-sm">
                                <i class="am-icon-cloud-upload"></i>选择要上传的文件</button>
                            <input id="file" type="file" name="file" >
                        </div>
                        <div id="file-list"></div>
                        <button type="submit" class="am-btn am-round am-btn-success"><span class="am-icon-upload"></span> 上传头像</button>
                        <script>
                            $(function() {
                                $('#file').on('change', function() {
                                    var fileNames = '';
                                    $.each(this.files, function() {
                                        fileNames += '<span class="am-badge">' + this.name + '</span> ';
                                    });
                                    $('#file-list').html(fileNames);
                                });
                            });
                        </script>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- content end -->
</div>
<a href="#" class="am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}">
    <span class="am-icon-btn am-icon-th-list"></span>
</a>
<jsp:include page="include/footer.jsp"/>
<script>
    if("${message}"){
        layer.msg('${message}', {
            offset: 0,
        });
    }
    if("${error}"){
        layer.msg('${error}', {
            offset: 0,
            shift: 6
        });
    }

    function checkFileType(){
        var format = ["bmp","jpg","gif","png"];
        var filename = $("#file").val();//C:\fakepath\avtar.png
        var ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();//png
        if(jQuery.inArray(ext, format) != -1){//函数用于在数组中搜索指定的值并返回其索引值;如果数组中不存在该值,则返回-1
            return true;
        }else{
            layer.msg('头像格式只能是bmp,jpg,gif,png!', {
                offset: 0,
                shift: 6
            });
            return false;
        }
    }
</script>
</body>
</html>

