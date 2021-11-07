package per.pay.business.framwork.api.entity;

/**
 * 支付状态枚举
 */
public enum PayStatusEnums {
    WAIT_PAY(0),//待支付
    PAID(1),//已支付
    FAILED(2),//支付失败
    EXPIRED(3)//已过期
    ;

    private int value;

    PayStatusEnums(int value) {
        this.value = value;
    }
}
