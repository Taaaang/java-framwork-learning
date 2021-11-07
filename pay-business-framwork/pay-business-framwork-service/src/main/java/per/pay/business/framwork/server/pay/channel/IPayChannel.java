package per.pay.business.framwork.server.pay.channel;

import java.util.List;
import java.util.Map;
import per.pay.business.framwork.api.entity.PayRequestBO;

public interface IPayChannel {


  /**
   * 0:支付类型标识符，参考 {@link per.pay.business.framwork.api.entity.PayTypeEnum} 1:提供商标识符,参考 {@link
   * per.pay.business.framwork.api.entity.PaySupplierEnum}
   *
   * @return 标识数组
   */
  String[] getChannelSign();

  /**
   * 通过支付渠道进行支付
   *
   * @param requestBO 请求参数
   * @return 第一步支付信息
   */
  PartnerPayResponse payByChannel(PayRequestBO requestBO);

  /**
   * 解析回调参数
   * @param parameterMap 回调参数
   * @return 同一格式的响应参数
   */
  PartnerCallbackResponse resolveCallbackParameter(Map<String, String[]> parameterMap);

  /**
   * 查询第三方支付信息
   * @param payId 我方支付单号
   * @return 响应参数
   */
  PartnerCallbackResponse queryPayInfo(long payId);

}
