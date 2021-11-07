package per.pay.business.framwork.server.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import per.pay.business.framwork.server.pay.PayService;

/**
 * @author: TangFenQi
 * @description: 支付定时任务
 * @date：2021/11/7 16:33
 */
@Component
public class PayTask {

  @Autowired
  private PayService service;

  public void updatePay(){
    service.renewPayStatus();
  }

}
