package per.pay.business.framwork.server.pay;

import per.pay.business.framwork.api.entity.PayRequestBO;

public interface IPayChannel {


    /**
     * 0:支付类型标识符，参考 {@link per.pay.business.framwork.api.entity.PayTypeEnum}
     * 1:提供商标识符
     * @return 标识数组
     */
    String[] getChannelSign();


    void payByChannel(PayRequestBO requestBO);

}
