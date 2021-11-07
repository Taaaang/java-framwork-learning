package per.pay.business.framwork.server.pay.channel;

import per.pay.business.framwork.server.support.PayPropertyLoader;

/**
 * @Author:TangFenQi
 * @Date:2021/11/6 22:48
 **/
public abstract class AbstractPayChannel implements IPayChannel {

    protected PayPropertyLoader propertyLoader;

    public AbstractPayChannel(PayPropertyLoader propertyLoader) {
        this.propertyLoader = propertyLoader;
    }
}
