package per.vertx.learn.server.road.config;

import per.vertx.learn.data.entity.Setting;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/14 - 5:01 下午
 **/
public class HttpRoadConfig implements RoadConfig {

    private final String parentPath;

    private Integer marketId;

    public HttpRoadConfig(Setting setting) {
        this.parentPath=setting.getParentPath();
        this.marketId=setting.getMarketId();
    }

    /**
     * 通过vin码获取车辆url
     * @return
     */
   public String getCarByVinUrl(){
       return parentPath+"/car";
   }

    /**
     * 绑定rfid的url
     * @return
     */
    public String bindRfidUrl(){
        return parentPath+"/bind/rfid";
    }

    /**
     * 车辆进场url
     * @return
     */
    public String carEntryUrl(){
        return parentPath+"/pass-record";
    }

    /**
     * 车辆出场url
     * @return
     */
    public String carOutUrl(){
        return parentPath+"/pass-record";
    }

    @Override
    public Integer getMarketId() {
        return marketId;
    }
}
