package per.pay.business.framwork.server.chain;

import per.pay.business.framwork.api.entity.PayRequestBO;

/**
 * 预处理扩展接口， 支持 {@link org.springframework.core.annotation.Order},值越大，越先执行
 */
public interface IPayPrepareHandler {

    boolean check(PayRequestBO requestBO);

}
