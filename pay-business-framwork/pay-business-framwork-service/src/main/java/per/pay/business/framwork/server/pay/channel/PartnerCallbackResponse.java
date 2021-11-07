package per.pay.business.framwork.server.pay.channel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import per.pay.business.framwork.api.entity.PayStatusEnums;

/**
 * @author: TangFenQi
 * @description: 第二阶段，第三方回调信息的统一格式
 * @date：2021/11/7 15:22
 */
@Data
public class PartnerCallbackResponse {

  private long payId;//我方支付单编号

  private BigDecimal money;//支付金额

  private LocalDateTime time;//支付时间

  private PayStatusEnums status;//支付状态

  private String successSign;//成功标识符

}
