package per.pay.business.framwork.server.pay;

import com.alibaba.csp.sentinel.SphU;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import per.pay.business.framwork.api.entity.PayRequestBO;
import per.pay.business.framwork.api.entity.PayResponseBO;
import per.pay.business.framwork.server.chain.PayPreparedCheck;
import per.pay.business.framwork.server.dao.PayBaseInfoDO;

@Component
public class PayService {

    @Autowired
    private PayManager payManager;
    @Autowired
    private PayPreparedCheck preparedChecker;


    public PayResponseBO pay(PayRequestBO requestBO){
        PayResponseBO resultBO=new PayResponseBO();
        //信息验证，鉴权，外部应用限流(chain)
        if(!preparedChecker.toCheck(requestBO)){
            return PayResponseBO.failed(PayResponseBO.ACCESS_LIMIT_ERROR,"完成准备阶段验证失败!");
        }
        //进行支付流程
        toPay(requestBO);
        //数据解析

        return resultBO;
    }

    /**
     * 创建支付信息
     */
    private PayResponseBO toPay(PayRequestBO payRequestBO){

        //托管给PayManager
        payManager.payByChannel(payRequestBO);

        //创建支付信息，进行初次存档
        PayBaseInfoDO payBaseInfoDO = initPayBaseInfo();

        //更新支付信息,记录相应支付的扩展表
        if(!updateSecondPayInfo()){

        };
        //返回结果

        return new PayResponseBO();
    }


    private PayBaseInfoDO initPayBaseInfo(){
        return new PayBaseInfoDO();
    }

    private boolean updateSecondPayInfo(){
        return true;
    }

}
