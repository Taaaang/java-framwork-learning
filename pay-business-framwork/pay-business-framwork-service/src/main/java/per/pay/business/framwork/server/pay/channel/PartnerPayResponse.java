package per.pay.business.framwork.server.pay.channel;

/**
 * @author: TangFenQi
 * @description: 第三方返回的数据信息
 * @date：2021/11/7 14:42
 */
public class PartnerPayResponse {

  private String data;//根据第三方需要与前端协商的json格式数据
  private String partnerPayId; //第三方的支付单号

}
