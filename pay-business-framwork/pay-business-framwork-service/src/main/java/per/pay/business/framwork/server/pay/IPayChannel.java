package per.pay.business.framwork.server.pay;

import per.pay.business.framwork.api.entity.PayRequestBO;
import per.pay.business.framwork.server.pay.channel.PartnerOfCallbackResponse;
import per.pay.business.framwork.server.pay.channel.PartnerOfPayResponse;

import java.util.Map;

public interface IPayChannel {


    /**
     * 0:支付类型标识符，参考 {@link per.pay.business.framwork.api.entity.PayTypeEnum}
     * 1:提供商标识符,参考 {@link per.pay.business.framwork.api.entity.PaySupplierEnum}
     * @return 标识数组
     */
    String[] getChannelSign();


    /**
     * 通过支付渠道进行支付
     * @param requestBO 请求参数
     * @param payId 支付单号
     * @return 响应参数
     */
    PartnerOfPayResponse payByChannel(PayRequestBO requestBO,long payId);

    /**
     * 将第三方回调数据，解析为统一格式
     * @param parameterMap 回调数据
     * @return 统一格式
     */
    PartnerOfCallbackResponse resolveParameter(Map<String, String[]> parameterMap);

    /**
     * 通过第三方查询订单信息
     * @param payId 支付单号
     * @return 订单信息
     */
    PartnerOfCallbackResponse queryPayInfo(long payId);

}
