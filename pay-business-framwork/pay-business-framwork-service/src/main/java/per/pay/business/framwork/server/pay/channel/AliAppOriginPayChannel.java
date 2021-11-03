package per.pay.business.framwork.server.pay.channel;

import org.springframework.stereotype.Component;
import per.pay.business.framwork.api.entity.PayRequestBO;
import per.pay.business.framwork.api.entity.PaySupplierEnum;
import per.pay.business.framwork.api.entity.PayTypeEnum;
import per.pay.business.framwork.server.pay.IPayChannel;

@Component
public class AliAppOriginPayChannel implements IPayChannel {

    private static final String[] SIGNS=new String[]{
            PayTypeEnum.ALI_APP.getSign(),
            PaySupplierEnum.Ali.getSign()
    };

    @Override
    public String[] getChannelSign() {
        return SIGNS;
    }

    @Override
    public void payByChannel(PayRequestBO requestBO) {
        //获取配置属性
        //填充必要参数
        //访问第三方
        //获取结果
        //组装返回
    }
}
