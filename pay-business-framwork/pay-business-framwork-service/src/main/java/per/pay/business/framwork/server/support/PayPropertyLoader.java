package per.pay.business.framwork.server.support;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Component
public class PayPropertyLoader {

    private List<IPayPropertyProvider> providers=new ArrayList<IPayPropertyProvider>(16);

    public IPayPropertyProvider getProperty(String[] signs){
        Assert.notNull(signs,"signs is empty! can not get properties");
        Assert.isTrue(signs.length>=2,"this signs not meet the specification ,at lease 2 sign!");
        if(providers.isEmpty()){
            throw new NullPointerException("pay property provider list are empty!");
        }
        Object property=null;
        for (IPayPropertyProvider provider : providers) {
            property=provider.getProperty(signs);
            if(property!=null){
                return provider;
            }
        }

        throw new IllegalArgumentException(String.format("no suitable pay property provider by [%s_%s]!",signs[0],signs[1]));
    }


}
