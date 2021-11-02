package per.pay.business.framwork.server.support;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 微信app原生支付属性加载器
 */
@Component
public class WeChatAppOriginPayPropertyProvider implements IPayPropertyProvider<WeChatAppOriginPayPropertyProvider.WeChatAppOriginPayProperty> {

    private static final String SIGN="WeChatAppOrigin";

    public WeChatAppOriginPayProperty getProperty(String channelSign) {
        if(!isAllow(channelSign)){
            return null;
        }
        return new WeChatAppOriginPayProperty();
    }

    public boolean isAllow(String channelSign) {
        Assert.hasText(channelSign,"channelSign is Empty ! pls check it");
        return SIGN.equals(channelSign.trim());
    }

    public static class WeChatAppOriginPayProperty{
        private String appId;
        private String mchId;
        private String notifyUrl="http://xxxxxxx";
        private String tradeType="APP";
    }
}
