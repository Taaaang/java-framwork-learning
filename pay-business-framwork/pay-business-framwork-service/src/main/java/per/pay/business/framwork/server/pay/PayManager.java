package per.pay.business.framwork.server.pay;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import per.pay.business.framwork.api.entity.PayRequestBO;
import per.pay.business.framwork.server.pay.channel.PartnerOfPayResponse;
import per.pay.business.framwork.server.pay.selector.AbstractPayChannelSelector;
import per.pay.business.framwork.server.support.IPayPropertyProvider;
import per.pay.business.framwork.server.support.PayPropertyLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class PayManager implements ApplicationContextAware, SmartInitializingSingleton {

    @Autowired
    private Gson gson;

    private List<AbstractPayChannelSelector> selectors = new ArrayList<>();

    /**
     * 支付渠道容器，Key:支付方式 {@link per.pay.business.framwork.api.entity.PayTypeEnum}，Value:支付渠道
     */
    private Map<String, List<IPayChannel>> channelsTypeMap = new HashMap<>();

    /**
     * 支付渠道容器,Key:支付提供商  {@link per.pay.business.framwork.api.entity.PaySupplierEnum},Value:支付渠道
     */
    private Map<String, List<IPayChannel>> suppliersMap = new HashMap<>();

    private ApplicationContext applicationContext;

    @Autowired
    private PayPropertyLoader propertyLoader = new PayPropertyLoader();
    //渠道分级，支付宝App，微信App，支付宝页面等

    public PartnerOfPayResponse payByChannel(PayRequestBO requestBO, long payId) {
        //通过筛选器 筛选支付通道
        IPayChannel payChannel = toSelector(requestBO);
        //通过支付通道进行支付请求
        return payChannel.payByChannel(requestBO,payId);
    }

    private IPayChannel toSelector(PayRequestBO payRequestBO) {
        List<IPayChannel> channelsCopy=null;
        if(payRequestBO.getPayType()!=null&&payRequestBO.getSupplierEnum()!=null){
            //1.指定支付方式与渠道
            IPayChannel payChannel = accurateGet(new String[]{payRequestBO.getPayType().getSign(), payRequestBO.getSupplierEnum().getSign()});
            channelsCopy = new ArrayList<>();
            channelsCopy.add(payChannel);
        }else if (payRequestBO.getPayType()!=null){
            //2.指定支付方式，未设置渠道
            String sign = payRequestBO.getPayType().getSign();
            //访问所有选择器
            channelsCopy = new ArrayList<>(channelsTypeMap.get(sign));
        }else {
            log.error("can not found payType! payRequestBO:{}",gson.toJson(payRequestBO));
            throw new IllegalArgumentException("can not found payType !");
        }
        //通过选择器进行筛选
        for (AbstractPayChannelSelector selector : selectors) {
            channelsCopy=selector.select(channelsCopy,payRequestBO);
        }
        //如果还有剩余的
        return channelsCopy.get(0);
    }


    /**
     * 根据 标识符 {@link IPayChannel}的getChannelSign()方法 获取渠道
     * @param signs 标识符
     * @return 渠道
     */
    public IPayChannel accurateGet(String[] signs){
        List<IPayChannel> channels = suppliersMap.get(signs[1]);
        Assert.notEmpty(channels,String.format("no suitable channels! signs supplier[%s]",signs[1]));
        for (IPayChannel channel : channels) {
            if(channel.getChannelSign()[0].equals(signs[0])){
                return channel;
            }
        }
        throw new IllegalArgumentException(String.format("no suitable channels! signs channel[%s],supplier[%s]",signs[0],signs[1]));
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, IPayChannel> channels = applicationContext.getBeansOfType(IPayChannel.class);
        for (Map.Entry<String, IPayChannel> entry : channels.entrySet()) {
            //支付方式
            String typeSign = entry.getValue().getChannelSign()[0];
            List<IPayChannel> types = channelsTypeMap.getOrDefault(typeSign, new ArrayList<>());
            types.add(entry.getValue());
            //支付提供商
            String supplierSign = entry.getValue().getChannelSign()[1];
            List<IPayChannel> suppliers = suppliersMap.getOrDefault(supplierSign, new ArrayList<>());
            suppliers.add(entry.getValue());
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
