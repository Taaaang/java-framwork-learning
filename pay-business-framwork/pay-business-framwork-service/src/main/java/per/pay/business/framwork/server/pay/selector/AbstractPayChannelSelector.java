package per.pay.business.framwork.server.pay.selector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import per.pay.business.framwork.api.entity.PayRequestBO;
import per.pay.business.framwork.server.pay.IPayChannel;
import per.pay.business.framwork.server.support.IPayPropertyProvider;
import com.google.gson.Gson;
import java.util.List;

@Slf4j
public abstract class AbstractPayChannelSelector {

    @Autowired
    private Gson gson;

    public List<IPayChannel> select(List<IPayChannel> list, PayRequestBO requestBO){
        List<IPayChannel> iPayChannels = toSelect(list, requestBO);
        if(iPayChannels==null||iPayChannels.isEmpty()){
            log.error("no suitable handler!  payRequestBO:[{}]",gson.toJson(requestBO));
            throw new IllegalArgumentException("no suitable handler!");
        }
        return iPayChannels;
    }

    public abstract List<IPayChannel> toSelect(List<IPayChannel> list,  PayRequestBO requestBO);

}
