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

}
