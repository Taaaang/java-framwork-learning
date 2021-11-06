package per.pay.business.framwork.server.controller;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import per.pay.business.framwork.api.entity.PaySupplierEnum;
import per.pay.business.framwork.api.entity.PayTypeEnum;
import per.pay.business.framwork.server.pay.PayService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author:TangFenQi
 * @Date:2021/11/6 21:45
 **/
@RequestMapping("/pay")
@RestController
@Slf4j
public class PayCallbackController {

    @Autowired
    private Gson gson;

    @Autowired
    private PayService payService;

    /**
     * 接收回调
     * @param request
     * @param channel 支付类型标识符， 参考 {@link per.pay.business.framwork.api.entity.PayTypeEnum}
     * @param supplier 提供商标识符， 参考 {@link per.pay.business.framwork.api.entity.PaySupplierEnum}
     * @return
     */
    @RequestMapping(value = "/{channel}/{supplier}",method = RequestMethod.POST)
    public String callback(HttpServletRequest request, @PathVariable String channel,@PathVariable String supplier){
        log.info("received pay channel:{} ,supplier:{}, callback info :{}",channel,supplier,gson.toJson(request.getParameterMap()));
        //验证标识
        verifySign(channel,supplier);
        String[] signs=new String[]{channel,supplier};
        //获取参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        //执行支付流程
        return payService.receivedCallback(signs, parameterMap);
    }

    private void verifySign(String channel,String supplier){
        Assert.hasText(channel,"channel sign is not text!");
        Assert.hasText(supplier,"supplier sign is not text!");
        PayTypeEnum.transform(channel);
        PaySupplierEnum.transform(supplier);
    }




}
