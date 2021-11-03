package per.pay.business.framwork.api.entity;

public enum PaySupplierEnum {
    Ali("Ali"),
    WeChat("WeChat");

    PaySupplierEnum(String sign) {
        this.sign = sign;
    }

    private String sign;

    public String getSign() {
        return sign;
    }
}
