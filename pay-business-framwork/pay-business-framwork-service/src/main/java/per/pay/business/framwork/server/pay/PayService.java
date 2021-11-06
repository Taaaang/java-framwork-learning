package per.pay.business.framwork.server.pay;

import com.alibaba.csp.sentinel.SphU;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import per.pay.business.framwork.api.entity.PayRequestBO;
import per.pay.business.framwork.api.entity.PayResponseBO;
import per.pay.business.framwork.server.chain.PayPreparedCheck;
import per.pay.business.framwork.server.dao.PayBaseInfoDO;
import per.pay.business.framwork.server.utils.IdWorker;

import java.util.Map;

@Component
public class PayService {

    @Autowired
    private PayManager payManager;
    @Autowired
    private PayPreparedCheck preparedChecker;

    private static final int workspace=1;
    private static final int machineId=1;

    /**
     * 第一阶段，创建支付信息
     * @param requestBO
     * @return
     */
    public PayResponseBO pay(PayRequestBO requestBO){
        PayResponseBO resultBO=new PayResponseBO();
        //信息验证，鉴权，外部应用限流(chain)
        if(!preparedChecker.toCheck(requestBO)){
            return PayResponseBO.failed(PayResponseBO.ACCESS_LIMIT_ERROR,"完成准备阶段验证失败!");
        }
        //进行支付流程
        PayResponseBO bo = toPay(requestBO);
        //数据解析

        return resultBO;
    }

    /**
     * 创建支付信息
     */
    private PayResponseBO toPay(PayRequestBO payRequestBO){
        //生成支付单id
        long payId = IdWorker.nextId(workspace, machineId);
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

    /**
     * 第二阶段，收到回调结果
     * @param signs 类型
     * @param parameterMap 参数值
     * @return 响应标识符
     */
    public String receivedCallback(String[] signs, Map<String, String[]> parameterMap){
        return null;
    }


    private PayBaseInfoDO initPayBaseInfo(){
        return new PayBaseInfoDO();
    }

    private boolean updateSecondPayInfo(){
        return true;
    }

}
