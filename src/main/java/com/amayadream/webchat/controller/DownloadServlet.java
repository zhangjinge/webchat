package com.amayadream.webchat.controller;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by dell on 2017/5/6.
 */
@WebServlet(name = "DownloadServlet",urlPatterns = {"/servlet/DownloadServlet"})
public class DownloadServlet extends HttpServlet {

    /**
     * Content-Type
     * Content-Disposition
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fileName="D:\\workspace\\webchat\\src\\main\\webapp\\static\\headers\\b\\3\\b316437d-0f3b-4c32-b693-9d00d6d1f2d5_avtar.png";
        String mimeType = this.getServletContext().getMimeType(fileName);
        response.setHeader("Content-Type",mimeType);
        response.setHeader("Content-Disposition","attachment;filename=header.png");
        FileInputStream fis=new FileInputStream(fileName);
        ServletOutputStream sos = response.getOutputStream();
        IOUtils.copy(fis,sos);//将输入流之中读到的文件写入到浏览器端的输出流之中
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request,response);
    }
}
