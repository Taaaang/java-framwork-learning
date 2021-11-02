package per.pay.business.framwork.server.chain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import per.pay.business.framwork.api.entity.PayRequestBO;

import java.util.*;

/**
 * 支付准备阶段的检测接口
 */
@Component
@Slf4j
public class PayPreparedCheck {

    private ArrayList<PayPrepareHandlerWrapper> checkers=new ArrayList<PayPrepareHandlerWrapper>();


    public boolean toCheck(PayRequestBO requestBO){
        for (PayPrepareHandlerWrapper checker : checkers) {
            IPayPrepareHandler handler = checker.handler;
            if(!handler.check(requestBO)){
               log.info("check failed className:{}",handler.getClass().getName());
               return false;
           }
        }
        return true;
    }

    public void registry(List<IPayPrepareHandler> handlers){
        Assert.notNull(handlers,"handler list is empty!");
        Assert.isTrue(handlers.size()!=0,"handler list is empty!");
        for (IPayPrepareHandler handler : handlers) {
            registry(handler);
        }
        checkers.sort((o1, o2) -> o1.order>o2.order?1:-1);
        log.debug("completed payPrepareHandler registration size:[{}].",handlers.size());
    }

    private void registry(IPayPrepareHandler handler){
        Assert.notNull(handler,"registration failed , invalid handler ");
        checkers.add(new PayPrepareHandlerWrapper(handler));
        log.debug("registered className:[{}]",handler.getClass().getName());
    }


    private class PayPrepareHandlerWrapper{
        private IPayPrepareHandler handler;
        private int order;

        public PayPrepareHandlerWrapper(IPayPrepareHandler handler){
            Order annotation = handler.getClass().getAnnotation(Order.class);
            this.order=annotation==null?0:annotation.value();
            this.handler=handler;
        }
    }

}
