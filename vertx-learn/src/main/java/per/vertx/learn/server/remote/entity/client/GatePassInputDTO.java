package per.vertx.learn.server.remote.entity.client;


import lombok.Data;

/**
 * @author tianzy
 * @date 2022年03月08日 09:32
 */
@Data
public class GatePassInputDTO {

    private String marketCarId;

    private String marketCode;

    //INBOUND,OUTBOUND
    private String passType;

    private Long passDate;

    private String gateName;

    private GatePassInputDTO(String marketCarId, String marketCode, String passType, Long passDate, String gateName) {
        this.marketCarId = marketCarId;
        this.marketCode = marketCode;
        this.passType = passType;
        this.passDate = passDate;
        this.gateName = gateName;
    }

    public static GatePassInputDTO entry(String marketCarId,String marketCode,String gateName){
        return new GatePassInputDTO(marketCarId,marketCode,"INBOUND",System.currentTimeMillis()/1000,gateName);
    }
    public static GatePassInputDTO entry(String marketCarId,String marketCode){
        return new GatePassInputDTO(marketCarId,marketCode,"INBOUND",System.currentTimeMillis()/1000,"默认入口");
    }
    public static GatePassInputDTO out(String marketCarId,String marketCode,String gateName){
        return new GatePassInputDTO(marketCarId,marketCode,"OUTBOUND",System.currentTimeMillis()/1000,gateName);
    }
    public static GatePassInputDTO out(String marketCarId,String marketCode){
        return new GatePassInputDTO(marketCarId,marketCode,"OUTBOUND",System.currentTimeMillis()/1000,"默认入口");
    }
}
