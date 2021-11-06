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

    public static PayTypeEnum transform(String name){
        if(null==name||name.isEmpty()){
            throw new RuntimeException("pay type name is empty!");
        }
        for (PayTypeEnum value : PayTypeEnum.values()) {
            if(value.getSign().equals(name)){
                return value;
            }
        }

        throw new RuntimeException("no suitable pay type enum by "+name);
    }

}
