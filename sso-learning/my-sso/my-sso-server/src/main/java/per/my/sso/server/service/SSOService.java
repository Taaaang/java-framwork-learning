package per.my.sso.server.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/1/6 - 2:00 下午
 **/
@Service
public class SSOService {

    private static Set<String> tokenSets=new HashSet<>();

    public boolean verifyToken(String ssoToken){
        return tokenSets.contains(ssoToken);
    }

    public String getToken(){
        String token=UUID.randomUUID().toString();
        tokenSets.add(token);
        return token;
    }

}
