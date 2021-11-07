package per.pay.business.framwork.server.pay;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import per.pay.business.framwork.api.entity.PayRequestBO;
import per.pay.business.framwork.api.entity.PayResponseBO;
import per.pay.business.framwork.api.entity.PayStatusEnums;
import per.pay.business.framwork.server.chain.PayPreparedCheck;
import per.pay.business.framwork.server.controller.PayCallbackController;
import per.pay.business.framwork.server.dao.PayBaseInfoDO;
import per.pay.business.framwork.server.pay.channel.IPayChannel;
import per.pay.business.framwork.server.pay.channel.PartnerCallbackResponse;
import per.pay.business.framwork.server.pay.channel.PartnerPayResponse;
import per.pay.business.framwork.server.utils.IdWorker;

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
  @Autowired
  private PayService myself;

  private static final int WORKSPACE = 1;
  private static final int MACHINE_ID = 1;

  /**
   * 第一阶段，创建支付信息
   */
  public PayResponseBO pay(PayRequestBO requestBO) {
    //信息验证，鉴权，外部应用限流(chain)
    if (!preparedChecker.toCheck(requestBO)) {
      return PayResponseBO.failed(PayResponseBO.ACCESS_LIMIT_ERROR, "完成准备阶段验证失败!");
    }
    //进行支付流程
    PayResponseBO bo = toPay(requestBO);
    //数据解析
    return bo;
  }

  /**
   * 创建支付信息
   */
  private PayResponseBO toPay(PayRequestBO payRequestBO) {
    //生成支付单id
    long payId = IdWorker.nextId(WORKSPACE, MACHINE_ID);
    //托管给PayManager
    PartnerPayResponse partnerResponse;
    try {
      partnerResponse = payManager.payByChannel(payRequestBO);
    } catch (Exception ex) {
      log.error("access partner failed ! pay request:{}", gson.toJson(payRequestBO));
      return PayResponseBO.failed(PayResponseBO.PARTNER_ERROR, ex.getMessage());
    }
    //创建支付信息，进行初次存档
    PayBaseInfoDO payBaseInfoDO = initPayBaseInfo(payId, partnerResponse, payRequestBO);
    //返回结果

    return new PayResponseBO();
  }

  /**
   * 第二阶段，收到回调结果
   *
   * @param signs 类型
   * @param parameterMap 参数值
   * @return 响应标识符
   */
  public String receivedCallback(String[] signs, Map<String, String[]> parameterMap) {
    //获取通道
    IPayChannel channel = payManager.accurateGet(signs);
    //解析回调参数为同一参数
    PartnerCallbackResponse response = channel
        .resolveCallbackParameter(parameterMap);
    //更新支付数据
    String successSign = toUpdate(response);
    return successSign;
  }

  //更新订单信息
  private String toUpdate(PartnerCallbackResponse response) {
    //TODO 更新订单信息
    if (response.getStatus().equals(PayStatusEnums.WAIT_PAY)) {
      //状态为等待支付，不更新信息
    } else if (response.getStatus().equals(PayStatusEnums.PAY)) {
      //状态为已支付，更新信息
      toSuccess(response);
    } else if (response.getStatus().equals(PayStatusEnums.FAILED)) {
      //状态为以失败，更新信息
      toFailed(response);
    } else if(response.getStatus().equals(PayStatusEnums.EXPIRED)){
      //状态为已过期，更新信息
      toExpired(response);
    }
    return response.getSuccessSign();
  }

  /**
   * 将订单状态更新为已成功
   */
  private void toSuccess(PartnerCallbackResponse response) {
  }

  /**
   * 将订单状态更新为失败
   */
  private void toFailed(PartnerCallbackResponse response) {
  }

  /**
   * 将订单状态更新为过期
   */
  private void toExpired(PartnerCallbackResponse response) {
  }

  //按照规则获取订单信息
  private List<PayBaseInfoDO> queryInfos() {
    //TODO
    return new ArrayList<>();
  }

  public void renewPayStatus(){
    //获取订单信息集合
    List<PayBaseInfoDO> payBaseInfoDOS = queryInfos();
    //更新
    payBaseInfoDOS.forEach(this::updatePayInfo);
  }

  private void updatePayInfo(PayBaseInfoDO payBaseInfoDO){
    //根据支付信息获取渠道信息
    String[] signs=new String[]{payBaseInfoDO.getPayType(),payBaseInfoDO.getSupplier()};
    IPayChannel channel = payManager.accurateGet(signs);
    //访问第三方
    PartnerCallbackResponse response = channel.queryPayInfo(payBaseInfoDO.getPayId());
    //更新数据
    toUpdate(response);
  }

  /**
   * 创建支付单信息
   *
   * @param payId 支付单号
   * @param partnerResponse 第三方返回结果
   * @param payRequestBO 业务方请求参数
   * @return 持久化信息
   */
  private PayBaseInfoDO initPayBaseInfo(long payId, PartnerPayResponse partnerResponse,
      PayRequestBO payRequestBO) {
    //根据业务进行持久化 TODO
    return new PayBaseInfoDO();
  }

}
