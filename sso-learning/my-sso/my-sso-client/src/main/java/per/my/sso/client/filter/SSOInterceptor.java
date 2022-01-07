package per.my.sso.client.filter;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/1/5 - 5:14 下午
 **/
public class SSOInterceptor implements HandlerInterceptor {

    private String ssoLoginUrl="http://oss.test.net:9900/login.html";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpServletRequest req=(HttpServletRequest)request;
        HttpServletResponse res=(HttpServletResponse)response;
        //获取访问url
        String requestUrl=request.getRequestURL().toString();
        //是否存在session?
        if(checkToken(request)){
            return true;
        }else {
            //session是否合理？
            //跳转到sso服务，进行统一登陆
            res.setStatus(302);
            res.sendRedirect(getSsoLoginUrl());
            return false;
        }
    }

    private boolean checkToken(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session==null){
            return false;
        }
        Object ssoToken = session.getAttribute("ssoToken");
        if(ssoToken==null){
            return false;
        }
        return true;
    }

    private String getSsoLoginUrl(){
        return ssoLoginUrl;
    }
}
