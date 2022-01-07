package per.my.sso.server.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import per.my.sso.server.service.SSOService;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/1/6 - 1:51 下午
 **/
@Component
public class SSOAuthFilter implements HandlerInterceptor {

    private SSOService ssoService;
    private String loginUrl="http://sso.test.net:9900/login.html";

    public SSOAuthFilter(SSOService ssoService){
        this.ssoService=ssoService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpServletRequest req=(HttpServletRequest)request;
        HttpServletResponse res=(HttpServletResponse)response;

        if(excludesUrl(req.getRequestURL().toString())){
            return true;
        }
        //1.判断是否存在sso的token,验证是否合理
        if(verifySSOToken(req)){
            //3.不合理则跳转到登陆页面
            res.sendRedirect(loginUrl);
            return false;
        }else {
            //4.合理则跳转回原来的访问页面
            return false;
        }
    }

    private boolean excludesUrl(String url){
        return true;
    }

    private boolean verifySSOToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("ssoToken")){
                String value = cookie.getValue();
                return true;
            }
        }
        return false;
    }
}
