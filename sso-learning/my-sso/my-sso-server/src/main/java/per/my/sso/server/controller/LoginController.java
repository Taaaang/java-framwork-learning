package per.my.sso.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import per.my.sso.server.service.SSOService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/1/6 - 1:32 下午
 **/
@RestController
@RequestMapping("/access")
public class LoginController {

    @Autowired
    private SSOService ssoService;

    @RequestMapping(value = "toLogin",method = RequestMethod.GET)
    public void login(HttpServletResponse response, HttpServletRequest request,
                      @RequestParam("username")String username,
                      @RequestParam("password")String password,
                      @RequestParam("page")String page){
        if(username.equals("username")&&password.equals("password")){
            String token = ssoService.getToken();
            response.setStatus(302);
            Cookie cookie=new Cookie("ssoToken",token);
            response.addCookie(cookie);
            try {
                response.sendRedirect(buildRedirectUrl(page,token));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("重定向失败！");
            }
        }else {
            throw new RuntimeException("账号或密码错误！");
        }

    }

    private String buildRedirectUrl(String page,String token){
        return new String(Base64Utils.decode(page.getBytes(StandardCharsets.UTF_8)))+"?ssoToken="+token;
    }

    @RequestMapping(value = "verify",method = RequestMethod.GET)
    public boolean verifyToken(@RequestParam("ssoToken")String ssoToken){
        return ssoService.verifyToken(ssoToken);
    }

}
