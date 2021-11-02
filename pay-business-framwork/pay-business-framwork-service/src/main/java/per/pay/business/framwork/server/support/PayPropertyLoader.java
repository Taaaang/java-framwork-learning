package per.pay.business.framwork.server.support;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Component
public class PayPropertyLoader {

    private List<IPayPropertyProvider> providers=new ArrayList<IPayPropertyProvider>(16);

    public Object getProperty(String channelSign){
        Assert.hasText(channelSign,"channelSign is empty! pls check it ");
        if(providers.isEmpty()){
            throw new NullPointerException("pay property provider list are empty!");
        }
        Object property=null;
        for (IPayPropertyProvider provider : providers) {
            property=provider.getProperty(channelSign);
            if(property!=null){
                return provider;
            }
        }

        throw new IllegalArgumentException(String.format("no suitable pay property provider by [%s]!",channelSign));
    }


}
