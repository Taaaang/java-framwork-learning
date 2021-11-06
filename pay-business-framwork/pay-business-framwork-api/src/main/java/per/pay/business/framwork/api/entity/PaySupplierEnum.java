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

    public static PaySupplierEnum transform(String name){
        if(null==name||name.isEmpty()){
            throw new RuntimeException("supplier name is empty!");
        }
        for (PaySupplierEnum value : PaySupplierEnum.values()) {
            if(value.getSign().equals(name)){
                return value;
            }
        }

        throw new RuntimeException("no suitable supplier enum by "+name);
    }

}
