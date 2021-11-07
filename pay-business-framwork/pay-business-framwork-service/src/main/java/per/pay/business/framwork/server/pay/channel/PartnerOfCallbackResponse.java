package per.pay.business.framwork.server.pay.channel;

import lombok.Data;
import per.pay.business.framwork.api.entity.PayStatusEnums;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author:TangFenQi
 * @Date:2021/11/7 22:12
 **/
@Data
public class PartnerOfCallbackResponse {

    private long payId;//支付单号

    private LocalDateTime time;//支付时间

    private BigDecimal money;//支付金额

    private PayStatusEnums status;//支付状态

    private String successSign;//响应标识，根据第三方协商

}
