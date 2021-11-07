package per.pay.business.framwork.server.pay.channel;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author:TangFenQi
 * @Date:2021/11/7 22:02
 **/
@Data
public class PartnerOfPayResponse {

    private String partnerPayId;//第三方支付单号
    private String data;//响应参数，需要跟客户端协商，唤起支付的参数
    private LocalDateTime time;//支付发起时间

}
