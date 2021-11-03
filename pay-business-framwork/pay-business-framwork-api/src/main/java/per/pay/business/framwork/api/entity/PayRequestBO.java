package per.pay.business.framwork.api.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayRequestBO {

    //业务订单号
    private Long orderId;

    //用户编号
    private Long userId;

    //付款金额
    private BigDecimal money;

    //支付方式
    private PayTypeEnum payType;

    //支付提供商，非必填，如果填写将直接命中该渠道
    private PaySupplierEnum supplierEnum;

}
