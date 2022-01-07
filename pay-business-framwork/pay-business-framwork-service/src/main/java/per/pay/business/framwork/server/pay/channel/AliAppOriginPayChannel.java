package per.pay.business.framwork.server.pay.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import per.pay.business.framwork.api.entity.PayRequestBO;
import per.pay.business.framwork.api.entity.PaySupplierEnum;
import per.pay.business.framwork.api.entity.PayTypeEnum;
import per.pay.business.framwork.server.pay.IPayChannel;
import per.pay.business.framwork.server.support.AliAppOriginPayPropertyProvider;
import per.pay.business.framwork.server.support.IPayPropertyProvider;
import per.pay.business.framwork.server.support.PayPropertyLoader;

import java.util.Map;

@Component
public class AliAppOriginPayChannel extends AbstractPayChannel {

    private static final String[] SIGNS=new String[]{
            PayTypeEnum.ALI_APP.getSign(),
            PaySupplierEnum.Ali.getSign()
    };


    @Autowired
    public AliAppOriginPayChannel(PayPropertyLoader propertyLoader) {
        super(propertyLoader);
    }

    @Override
    public String[] getChannelSign() {
        return SIGNS;
    }

    @Override
    public PartnerOfPayResponse payByChannel(PayRequestBO requestBO,long payId) {
        //获取配置属性
        IPayPropertyProvider<AliAppOriginPayPropertyProvider.AllAppOriginPayProperty> property = propertyLoader.getPropertyProvider(SIGNS);
        AliAppOriginPayPropertyProvider.AllAppOriginPayProperty properties = property.getProperty(SIGNS);
        property.buildProperty(properties);
        //填充必要参数
        //访问第三方
        //获取结果
        //组装返回
        //TODO
        return null;
    }

    @Override
    public PartnerOfCallbackResponse resolveParameter(Map<String, String[]> parameterMap) {
        //TODO 根据第三方协议实现
        return new PartnerOfCallbackResponse();
    }

    @Override
    public PartnerOfCallbackResponse queryPayInfo(long payId) {
        return null;
    }
}
