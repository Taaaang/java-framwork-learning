package per.my.sso.web.two.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/1/7 - 10:16 上午
 **/
@RequestMapping("two")
@Controller
public class ServiceController {

    @RequestMapping("test")
    public String test(){
        return "server";
    }

}
