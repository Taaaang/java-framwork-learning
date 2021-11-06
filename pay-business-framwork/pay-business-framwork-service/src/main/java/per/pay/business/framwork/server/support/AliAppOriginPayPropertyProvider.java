package per.pay.business.framwork.server.support;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import per.pay.business.framwork.api.entity.PaySupplierEnum;
import per.pay.business.framwork.api.entity.PayTypeEnum;

/**
 * 支付宝原生app支付属性加载器
 */
@Component
public class AliAppOriginPayPropertyProvider implements IPayPropertyProvider<AliAppOriginPayPropertyProvider.AllAppOriginPayProperty> {

    private static final String[] SIGN=new String[]{
            PayTypeEnum.ALI_APP.getSign(),
            PaySupplierEnum.Ali.getSign()
    };


    public AllAppOriginPayProperty getProperty(String[] channelSign) {
        if(!isAllow(channelSign)){
            return null;
        }
        AllAppOriginPayProperty allAppOriginPayProperty=new AllAppOriginPayProperty();
        return allAppOriginPayProperty;
    }

    @Override
    public void buildProperty(AllAppOriginPayProperty properties) {
        //TODO
        return;
    }

    public boolean isAllow(String[] channelSign) {
        return SIGN[0].equals(channelSign[0].trim())&&
                SIGN[1].equals(channelSign[1].trim());
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
