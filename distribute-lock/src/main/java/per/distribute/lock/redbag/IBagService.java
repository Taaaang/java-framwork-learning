package per.distribute.lock.redbag;

import java.math.BigDecimal;

/**
 * 红包功能方法
 */
public interface IBagService {

    String RED_BAG_PREFIX = "redbag_";

    /**
     * 填充红包
     * @param money 金额
     * @param userId 发包用户编号
     * @return 红包编号
     */
    String fill(Long userId,BigDecimal money);

    /**
     * 拿取红包
     * @param redBagId 红包编号
     * @param userId 发包用户编号
     * @param takeUserId 领包用户编号
     * @return 获得金额
     */
    BigDecimal take(Long userId,Long takeUserId,String redBagId);



}
