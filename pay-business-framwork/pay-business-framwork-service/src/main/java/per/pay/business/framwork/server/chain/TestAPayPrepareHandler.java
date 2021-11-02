package per.pay.business.framwork.server.chain;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import per.pay.business.framwork.api.entity.PayRequestBO;

@Component
@Order(10)
public class TestAPayPrepareHandler implements IPayPrepareHandler {
    public boolean check(PayRequestBO requestBO) {
        return false;
    }
}
