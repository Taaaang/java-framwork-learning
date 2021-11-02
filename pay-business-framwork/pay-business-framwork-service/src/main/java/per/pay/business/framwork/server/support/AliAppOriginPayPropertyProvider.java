package per.pay.business.framwork.server.support;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 支付宝原生app支付属性加载器
 */
@Component
public class AliAppOriginPayPropertyProvider implements IPayPropertyProvider<AliAppOriginPayPropertyProvider.AllAppOriginPayProperty> {

    private static final String SIGN="AliAppOrigin";


    public AllAppOriginPayProperty getProperty(String channelSign) {
        if(!isAllow(channelSign)){
            return null;
        }
        AllAppOriginPayProperty allAppOriginPayProperty=new AllAppOriginPayProperty();
        return allAppOriginPayProperty;
    }

    public boolean isAllow(String channelSign) {
        Assert.hasText(channelSign,"channelSign is Empty ! pls check it");
        return SIGN.equals(channelSign.trim());
    }


    public static class AllAppOriginPayProperty{
        private String appId;
        private String method;
        private String charset="utf-8";
        private String signType="RSA2";
        private String version="1.0";
        private String notifyUrl="http://xxxxxx";
    }
}
