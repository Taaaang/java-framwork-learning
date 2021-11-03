package per.pay.business.framwork.server.support;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import per.pay.business.framwork.api.entity.PaySupplierEnum;
import per.pay.business.framwork.api.entity.PayTypeEnum;

/**
 * 微信app原生支付属性加载器
 */
@Component
public class WeChatAppOriginPayPropertyProvider implements IPayPropertyProvider<WeChatAppOriginPayPropertyProvider.WeChatAppOriginPayProperty> {

    private static final String[] SIGN=new String[]{
            PayTypeEnum.WECHAT_APP.getSign(),
            PaySupplierEnum.WeChat.getSign()
    };

    public WeChatAppOriginPayProperty getProperty(String[] channelSign) {
        if(!isAllow(channelSign)){
            return null;
        }
        return new WeChatAppOriginPayProperty();
    }

    public boolean isAllow(String[] channelSign) {
        return SIGN[0].equals(channelSign[0].trim())&&
                SIGN[1].equals(channelSign[1].trim());
    }

    public static class WeChatAppOriginPayProperty{
        private String appId;
        private String mchId;
        private String notifyUrl="http://xxxxxxx";
        private String tradeType="APP";
    }
}
