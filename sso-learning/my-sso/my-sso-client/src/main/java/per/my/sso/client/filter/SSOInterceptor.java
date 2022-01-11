package per.my.sso.client.filter;

import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/1/5 - 5:14 下午
 **/
public class SSOInterceptor implements HandlerInterceptor {

    private String ssoLoginUrl="http://oss.test.net:9900/login.html";
    private String ssoVerifyUrl="http://oss.test.net:9900/access/verify?ssoToken={ssoToken}&page={page}";
    private RestTemplate restTemplate;

    public SSOInterceptor(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //获取访问url
        String requestUrl=request.getRequestURL().toString();
        //是否存在session?
        if(checkToken(request)){
            return true;
        }else {
            //session是否合理？
            //跳转到sso服务，进行统一登陆
            response.setStatus(302);
            response.sendRedirect(getSsoLoginUrl(requestUrl));
            return false;
        }
    }

    private boolean checkToken(HttpServletRequest request){
        boolean status = checkBySession(request);
        if(status){
            return true;
        }
        status=checkByArgument(request);
        return status;
    }
    private boolean checkByArgument(HttpServletRequest request){
        String ssoToken = request.getParameter("ssoToken");
        if(StringUtils.isEmpty(ssoToken)){
            return false;
        }
        Map<String,String> valueMap=new HashMap<>();
        valueMap.put("ssoToken",ssoToken);
        valueMap.put("page",new String(Base64Utils.encode(request.getRequestURL().toString().getBytes(StandardCharsets.UTF_8))));
        Boolean ssoStatus = restTemplate.getForObject(ssoVerifyUrl, boolean.class, valueMap);
        if(ssoStatus==null){
            return false;
        }
        if(ssoStatus){
            buildSession(request);
        }
        return ssoStatus;
    }

    private boolean checkBySession(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session==null){
            return false;
        }
        Object isLogin = session.getAttribute("isLogin");
        if(isLogin==null){
            return false;
        }
        return (boolean)isLogin;
    }

    private void buildSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("isLogin",true);
        //session.setMaxInactiveInterval(10);
    }

    private String getSsoLoginUrl(String requestUrl){
        return ssoLoginUrl+"?page="+new String(Base64Utils.encode(requestUrl.getBytes(StandardCharsets.UTF_8)));
    }
}
