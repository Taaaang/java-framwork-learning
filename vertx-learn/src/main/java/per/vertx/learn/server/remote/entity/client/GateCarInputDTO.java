package per.vertx.learn.server.remote.entity.client;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author tianzy
 * @date 2022年03月07日 15:04
 */
@Data
@AllArgsConstructor
public class GateCarInputDTO {

    private String vin;

    private String marketCode;

}
