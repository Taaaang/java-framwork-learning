package per.vertx.learn.server.remote.entity.client;

import lombok.Data;


import java.util.HashMap;
import java.util.Map;

/**
 * @author tianzy
 * @date 2022年03月08日 09:18
 */
@Data
public class GateBindInputDTO {

    private String marketCarId;

    private String marketCode;

    private String rfid;

}
