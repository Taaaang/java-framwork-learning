package per.vertx.learn.server.remote.entity.client;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * bzt道闸待出库请求参数
 *
 * @author tianzy
 * @date 2022年03月09日 09:22
 */
@Data
@NoArgsConstructor
public class GateOutboundInputDTO implements Serializable {
    /**
     * 车辆id
     */
    private String marketCarId;
    /**
     * 车牌
     */
    private String plateNumber;
    /**
     * 市场主键
     */
    private String buNo;
    /**
     * 1-待入库/2-已入库/3-待出库/4-已出库
     */
    private Integer stockStatus;
    /**
     * 车型信息
     */
    private String brandName;
    /**
     * 车商名称
     */
    private String shopName;

}
