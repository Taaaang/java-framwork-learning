package per.vertx.learn.server.road;

import javafx.scene.text.Text;
import per.vertx.learn.data.entity.RoadCar;
import per.vertx.learn.server.remote.entity.GateCarOutputDTO;
import per.vertx.learn.view.wrapper.TipTextWrapper;

import java.util.List;

/**
 * 道闸功能
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/14 - 10:10 上午
 **/
public interface RoadService {

    /**
     * 车辆进场
     * @param carId 车辆id
     */
    void inMarket(String carId, TipTextWrapper text);

    /**
     * 车辆出场
     * @param carId 车辆id
     */
    void outMarket(String carId, TipTextWrapper text);

    /**
     * 车辆待出库
     * @param carId
     */
    void waitOutMarket(String carId);

    /**
     * 绑定rfid
     * @param carId 车辆id
     * @return 绑定的rfid
     */
    String bindRfid(String carId,TipTextWrapper text);

    /**
     * 解绑rfid
     * @param carId 车辆id
     */
    void cancelRfid(String carId);

    /**
     * 获取车辆信息
     * @param vin 车辆id
     * @return 车辆信息
     */
    List<GateCarOutputDTO> searchCarInfo(String vin);

}
