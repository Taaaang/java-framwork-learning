package per.pay.business.framwork.api.entity;

/**
 * 支付类型，分为支付宝App，微信app，支付宝页面等方式
 */
public enum PayTypeEnum {
    ALI_APP("AliApp"),
    WECHAT_APP("WeChatApp");

    private String sign;

    PayTypeEnum(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }
}
