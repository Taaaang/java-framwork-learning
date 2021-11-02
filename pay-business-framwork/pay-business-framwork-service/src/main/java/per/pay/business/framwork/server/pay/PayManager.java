package per.pay.business.framwork.server.pay;

import org.springframework.stereotype.Component;
import per.pay.business.framwork.api.entity.PayRequestBO;
import per.pay.business.framwork.server.pay.selector.AbstractPayChannelSelector;

import java.util.ArrayList;
import java.util.List;

@Component
public class PayManager {

    private List<AbstractPayChannelSelector> selectors=new ArrayList<>();
    private List<IPayChannel> channels=new ArrayList<>();

    //渠道分级，支付宝App，微信App，支付宝页面等

    private IPayChannel toSelector(PayRequestBO payRequestBO){
        //获取属性

        //访问所有选择器
        for (AbstractPayChannelSelector selector : selectors) {
            selector.toSelect(channels,null,payRequestBO);
        }
        return null;
    }



}
