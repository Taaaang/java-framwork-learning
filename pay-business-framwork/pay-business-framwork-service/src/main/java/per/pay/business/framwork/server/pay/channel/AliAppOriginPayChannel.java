package per.pay.business.framwork.server.pay.channel;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import per.pay.business.framwork.api.entity.PayRequestBO;
import per.pay.business.framwork.api.entity.PaySupplierEnum;
import per.pay.business.framwork.api.entity.PayTypeEnum;
import per.pay.business.framwork.server.support.AliAppOriginPayPropertyProvider;
import per.pay.business.framwork.server.support.IPayPropertyProvider;
import per.pay.business.framwork.server.support.PayPropertyLoader;

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
    public PartnerPayResponse payByChannel(PayRequestBO requestBO) {
        //获取配置属性
        IPayPropertyProvider<AliAppOriginPayPropertyProvider.AllAppOriginPayProperty> property = propertyLoader.getPropertyProvider(SIGNS);
        AliAppOriginPayPropertyProvider.AllAppOriginPayProperty properties = property.getProperty(SIGNS);
        property.buildProperty(properties);
        //填充必要参数

        //访问第三方
        //获取结果
        //组装返回
        return new PartnerPayResponse();
    }

    /**
     * 解析回调参数
     *
     * @param parameterMap 回调参数
     * @return 同一格式的响应参数
     */
    @Override
    public PartnerCallbackResponse resolveCallbackParameter(Map<String, String[]> parameterMap) {
        return null;
    }

    /**
     * 查询第三方支付信息
     *
     * @param payId 我方支付单号
     * @return 响应参数
     */
    @Override
    public PartnerCallbackResponse queryPayInfo(long payId) {
        return null;
    }
}
