package per.vertx.learn.server.remote;

import javafx.scene.text.Text;
import per.vertx.learn.server.remote.entity.GateCarOutputDTO;
import per.vertx.learn.view.wrapper.TipTextWrapper;

import java.util.List;

/**
 * 通知远程服务
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/14 - 11:11 上午
 **/
public interface NotifyService {

    /**
     * 通知车辆进场
     * @param carId 车辆id
     */
    void notifyCarInMarket(String carId, TipTextWrapper text);

    /**
     * 通知车辆出场
     * @param carId 车辆id
     */
    void notifyCaroutMarket(String carId, TipTextWrapper text);

    /**
     * 通知车辆绑定rfid
     * @param carId 车辆id
     * @param rfid rfid
     * @return 绑定的rfid
     */
    void notifyCarBindRfid(String carId,String rfid,TipTextWrapper text);

    /**
     * 通过vin码查询远程车辆信息
     * @param vin vin码
     * @return 车辆信息
     */
    List<GateCarOutputDTO> searchCar(String vin);
}
