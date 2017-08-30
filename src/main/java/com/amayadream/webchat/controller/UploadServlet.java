package com.amayadream.webchat.controller;

import com.amayadream.webchat.pojo.User;
import com.amayadream.webchat.service.UploadService;
import com.amayadream.webchat.serviceImpl.UploadServiceImpl;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 上传对表单限制;method="post";enctype="multipart/form-data";表单中需要添加文件表单项：<input type="file" name="xxx" />
 * request.getParameter("xxx");这个方法在表单为enctype="multipart/form-data"时,它永远都返回null
 * ServletInputStream request.getInputStream();包含整个请求的体！
 * 每隔出多个部件，即一个表单项一个部件
 * 一个部件中自己包含请求头和空行以及请求体。
 * 普通表单项：1个头：Content-Disposition：包含name="xxxx",即表单项名称;体就是表单项的值
 * 文件表单项：Content-Disposition：包含name="xxxx"，即表单项名称；还有一个filename="xxx"，表示上传文件的名称
 * Content-Type：它是上传文件的MIME类型，例如：image/pjpeg，表示上传的是图片，图上中jpg扩展名的图片;体就是上传文件的内容
 * commons-fileupload.jar
 * commons-io.jar
 * FileItem类常用API
 * boolean isFormField()：是否为普通表单项！返回true为普通表单项，如果为false即文件表单项！
 * String getFieldName()：返回当前表单项的名称；
 * String getString(String charset)：返回表单项的值；
 * String getName()：返回上传的文件名称
 * long getSize()：返回上传文件的字节数
 * InputStream getInputStream()：返回上传文件对应的输入流
 * void write(File destFile)：把上传的文件内容保存到指定的文件中。
 * String getContentType();
 */
@WebServlet(name = "UploadServlet",urlPatterns={"/servlet/UploadServlet"})
public class UploadServlet extends HttpServlet {

    private UploadService uploadService=new UploadServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;utf-8");
        DiskFileItemFactory factory = new DiskFileItemFactory();//创建工厂
        ServletFileUpload parser = new ServletFileUpload(factory);//创建解析器
        parser.setFileSizeMax(100*1024);
        try {
            List<FileItem> fileItemList = parser.parseRequest(request);
            for (FileItem fi:fileItemList){
                if(!fi.isFormField()){//判断是否是文件表单项
                    String fileName = fi.getName();
                    int index = fileName.lastIndexOf("\\");//c:apath/filename.png
                    if(index!=-1){//如果浏览器传递给后台的是绝对路径
                        fileName=fileName.substring(index+1);//转换成filename.png
                    }
                    System.out.println(fileName+","+fi.getSize()+","+fi.getContentType());//avtar.png;12253;image/png
                    String saveName= UUID.randomUUID().toString()+"_"+fileName;//动态前缀防止重名

                    String root = this.getServletContext().getRealPath("/static/headers");//目录打散
                    int hashCode = fileName.hashCode();
                    String hCode = Integer.toHexString(hashCode);
                    String path=hCode.charAt(0)+"/"+hCode.charAt(1);
                    File dirFile=new File(root,path);//使用hash值前两位创建目录
                    dirFile.mkdirs();

                    File destFile=new File(dirFile,saveName);
                    fi.write(destFile);

                    String rPath="static/headers/"+path+"/"+saveName;
                    User user =(User) request.getSession().getAttribute("user");
                    user.setProfilehead(rPath);
                    User _user = uploadService.addHeaderImg(user.getUserid(), user);
                    request.getSession().setAttribute("user",_user);
                    request.getRequestDispatcher("/WEB-INF/view/upload.jsp").forward(request,response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request,response);
    }
}
