package per.pay.business.framwork.api.entity;

import lombok.Data;

@Data
public class PayResponseBO {

    private static final int SUCCESS=0;//成功

    public static final int SYSTEM_ERROR=100;//系统内部为止错误
    public static final int PARTNER_ERROR=101;//访问第三方失败
    public static final int ACCESS_LIMIT_ERROR=200;//访问受限

    //状态码
    private int code;

    //当code为0时，填充对应json格式的数据
    private String data;

    //当code非0时存储错误提示信息
    private String errorMessage;

    private PayResponseBO(){}

    public static PayResponseBO success(String data){
        PayResponseBO result=new PayResponseBO();
        result.code=SUCCESS;
        result.data=data;
        return result;
    }

    public static PayResponseBO failed(int code,String errorMessage){
        PayResponseBO result=new PayResponseBO();
        result.code=code;
        result.errorMessage=errorMessage==null?"":errorMessage;
        return result;
    }

}
