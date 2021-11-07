package per.pay.business.framwork.server.pay;

import com.alibaba.csp.sentinel.SphU;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.util.Assert;
import per.pay.business.framwork.api.entity.PayRequestBO;
import per.pay.business.framwork.api.entity.PayResponseBO;
import per.pay.business.framwork.api.entity.PayStatusEnums;
import per.pay.business.framwork.server.chain.PayPreparedCheck;
import per.pay.business.framwork.server.dao.PayBaseInfoDO;
import per.pay.business.framwork.server.pay.channel.PartnerOfCallbackResponse;
import per.pay.business.framwork.server.pay.channel.PartnerOfPayResponse;
import per.pay.business.framwork.server.utils.IdWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class PayService {

    @Autowired
    private PayManager payManager;
    @Autowired
    private PayPreparedCheck preparedChecker;
    @Autowired
    private Gson gson;

    private static final int workspace=1;
    private static final int machineId=1;

    /**
     * 第一阶段，创建支付信息
     * @param requestBO
     * @return
     */
    public PayResponseBO pay(PayRequestBO requestBO){
        //信息验证，鉴权，外部应用限流(chain)
        if(!preparedChecker.toCheck(requestBO)){
            return PayResponseBO.failed(PayResponseBO.ACCESS_LIMIT_ERROR,"完成准备阶段验证失败!");
        }
        //进行支付流程
        PayResponseBO bo = toPay(requestBO);
        return bo;
    }

    /**
     * 创建支付信息
     */
    private PayResponseBO toPay(PayRequestBO payRequestBO){
        //生成支付单id
        long payId = IdWorker.nextId(workspace, machineId);
        //托管给PayManager
        PartnerOfPayResponse partnerOfPayResponse;
        try {
            partnerOfPayResponse=payManager.payByChannel(payRequestBO, payId);
        }catch (Exception ex){
            log.error("access partner by channel is failed! request:{}",gson.toJson(payRequestBO));
            throw new RuntimeException("access partner by channel is failed !");
        }
        //创建支付信息，进行初次存档
        PayBaseInfoDO payBaseInfoDO = initPayBaseInfo(partnerOfPayResponse,payRequestBO);
        //返回结果
        return PayResponseBO.success(partnerOfPayResponse.getData());
    }

    /**
     * 第二阶段，收到回调结果
     * @param signs 类型
     * @param parameterMap 参数值
     * @return 响应标识符
     */
    public String receivedCallback(String[] signs, Map<String, String[]> parameterMap){
        //根据signs获取channel
        IPayChannel payChannel = payManager.accurateGet(signs);
        //根据channel转换成统一格式
        PartnerOfCallbackResponse partnerOfCallbackResponse = payChannel.resolveParameter(parameterMap);
        //更新订单状态
        toUpdate(partnerOfCallbackResponse);
        return partnerOfCallbackResponse.getSuccessSign();
    }

    /**
     * 更新支付单信息
     */
    public void renewPayInfo(){
        //获取支付信息 TODO
        List<PayBaseInfoDO> list=new ArrayList<>();
        list.forEach(v->{
            //根据订单类型获取渠道
            IPayChannel payChannel = payManager.accurateGet(new String[]{v.getPayType(), v.getSupplier()});
            //通过渠道查询信息
            PartnerOfCallbackResponse partnerOfCallbackResponse = payChannel.queryPayInfo(v.getPayId());
            //更新订单信息
            toUpdate(partnerOfCallbackResponse);
        });
    }

    private void toUpdate(PartnerOfCallbackResponse response){
        Assert.notNull(response.getStatus(),"pay status is empty!");
        PayStatusEnums payStatus = response.getStatus();
        if(payStatus.equals(PayStatusEnums.WAIT_PAY)){
            //待支付，不做任何处理
        }else if(payStatus.equals(PayStatusEnums.PAID)){
            toSuccess(response);
        }else if(payStatus.equals(PayStatusEnums.FAILED)){
            toFailed(response);
        }else if(payStatus.equals(PayStatusEnums.EXPIRED)){
            toExpired(response);
        }else {
            log.error("no suitable method to status[{}]!",response.getStatus().name());
        }
    }

    private void toSuccess(PartnerOfCallbackResponse response){
        //TODO 更新状态，发送信息
    }

    private void toFailed(PartnerOfCallbackResponse response){
        //TODO 更新状态，发送信息
    }

    private void toExpired(PartnerOfCallbackResponse response){
        //TODO 更新状态,发送信息
    }

    /**
     * 建立待支付信息
     * @param partnerOfPayResponse 第三方响应结果
     * @param payRequestBO 支付请求参数
     * @return 支付信息
     */
    private PayBaseInfoDO initPayBaseInfo(PartnerOfPayResponse partnerOfPayResponse,PayRequestBO payRequestBO){
        return new PayBaseInfoDO();
    }

}
