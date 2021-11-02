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

    public List<IPayChannel> toSelect(List<IPayChannel> list, IPayPropertyProvider property, PayRequestBO requestBO){
        if(list==null||list.isEmpty()){
            log.error("no suitable handler! property class name:[{}] payRequestBO:[{}]",property.getClass().getName(),gson.toJson(requestBO));
            throw new IllegalArgumentException("no suitable handler!");
        }
        return list;
    }

}
