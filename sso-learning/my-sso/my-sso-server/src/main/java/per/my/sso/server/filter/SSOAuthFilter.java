package per.my.sso.server.filter;

import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import per.my.sso.server.service.SSOService;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
            throws Exception {

        //1.判断使用已经登陆过了,验证是否合理
        String ssoToken = getSSOToken(req);
        if(verifySSOToken(ssoToken)){
            //4.合理则跳转回原来的访问页面
            String page = req.getParameter("page");
            res.setStatus(302);
            res.sendRedirect(new String(Base64Utils.decode(page.getBytes(StandardCharsets.UTF_8)))+"?ssoToken="+ssoToken);
            return false;
        }else {
            //3.不合理则跳转到登陆页面
            res.setStatus(302);
            res.sendRedirect(loginUrl);
            return false;
        }
    }

    private boolean excludesUrl(String url){
        return true;
    }

    private boolean verifySSOToken(String token){
        return ssoService.verifyToken(token);
    }

    private String getSSOToken(HttpServletRequest request){
        if(request.getCookies()!=null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("ssoToken")) {
                    return cookie.getValue();
                }
            }
        }
        String ssoToken = request.getParameter("ssoToken");
        if(!StringUtils.isEmpty(ssoToken)){
            return ssoToken;
        }
        return null;
    }
}
