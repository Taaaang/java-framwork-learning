package per.pay.business.framwork.server.support;

/**
 * 定义获取渠道的属性配置
 * @param <T>
 */
public interface IPayPropertyProvider<T> {

    /**
     * 获取属性配置
     * @return 属性配置
     */
    T getProperty(String channelSign);

    /**
     * 判断是否是该渠道的属性配置
     * @param channelSign 渠道标识
     * @return true:是该渠道，false:不是该渠道
     */
    boolean isAllow(String channelSign);

}
