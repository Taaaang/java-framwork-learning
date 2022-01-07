package per.my.sso.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import per.my.sso.server.filter.SSOAuthFilter;
import per.my.sso.server.service.SSOService;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/1/6 - 2:50 下午
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SSOAuthFilter filter;
    @Autowired
    private SSOService ssoService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SSOAuthFilter(ssoService));
    }

}
