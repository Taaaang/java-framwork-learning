package per.pay.business.framwork.api.entity;

/**
 * @author: TangFenQi
 * @description: 支付状态
 * @date：2021/11/7 15:26
 */
public enum PayStatusEnums {
  WAIT_PAY(1),//第一阶段发起成功后的状态,等待支付信息
  PAY(2),//已支付状态
  FAILED(3),//失败状态
  EXPIRED(4)//超时状态
  ;

  private int value;

  PayStatusEnums(int value) {
    this.value = value;
  }
}
