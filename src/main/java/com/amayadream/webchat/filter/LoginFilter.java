package com.amayadream.webchat.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dell on 2017/4/23.
 */
public class LoginFilter extends OncePerRequestFilter{

    protected static final String URI="/login.jsp";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        System.out.println(contextPath);//,/webchat
        String uri = request.getRequestURI();//,/webchat/
        System.out.println(uri);
        if(uri.indexOf("/login.jsp")!=-1){
            if(null==request.getSession().getAttribute("user")){
                System.out.println(uri+">>>未登录,请先去登陆");
                request.getRequestDispatcher("/user/toLogin").forward(request,response);
                return;
            }else {
                System.out.println(uri+">>>已经登录,放行!");
                request.getRequestDispatcher("/user/toIndex").forward(request,response);
                return;
            }
        }else{
            System.out.println(uri+">>>未识别的资源放行!");
            filterChain.doFilter(request,response);
        }

    }
}
