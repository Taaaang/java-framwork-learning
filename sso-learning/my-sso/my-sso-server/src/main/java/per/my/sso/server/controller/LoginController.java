package per.my.sso.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/1/6 - 1:32 下午
 **/
@RestController
@RequestMapping("/access")
public class LoginController {


    @RequestMapping(value = "toLogin",method = RequestMethod.POST)
    public void login(HttpServletResponse response, HttpServletRequest request,
                      @RequestParam("username")String username,@RequestParam("password")String password){
        if(username.equals("username")&&password.equals("password")){
            request.getSession().setMaxInactiveInterval(30);
            request.getSession().setAttribute("isLogin",true);
        }
    }

    @RequestMapping(value = "login",method = RequestMethod.GET)
    public String jumpLoginPage(){
        return "";
    }

}
