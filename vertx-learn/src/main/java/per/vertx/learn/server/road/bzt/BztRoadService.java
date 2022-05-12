package per.vertx.learn.server.road.bzt;

import com.google.gson.Gson;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import javafx.scene.text.Text;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import per.vertx.learn.data.LocalDataService;
import per.vertx.learn.data.entity.RoadCar;
import per.vertx.learn.server.remote.NotifyService;
import per.vertx.learn.server.remote.entity.GateCarOutputDTO;
import per.vertx.learn.server.remote.entity.client.GateOutboundInputDTO;
import per.vertx.learn.server.remote.entity.server.HttpResponse;
import per.vertx.learn.server.road.AbstractRoadService;
import per.vertx.learn.view.wrapper.TipTextWrapper;

import java.util.List;
import java.util.UUID;

/**
 * 比之特道闸系统
 *
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/14 - 11:33 上午
 **/
@Slf4j
public class BztRoadService extends AbstractRoadService {

    /**
     * 二手车信息变更
     */
    private static final String CHANGE_USED_CAR = "/v1.0/gate/changeUsedCar";

    /**
     * 查询车辆信息
     */
    private static final String GET_USED_CAR = "/v1.0/gate/getUsedCarRfId";

    private static final Gson GSON = new Gson();

    private static final int PORT = Integer.parseInt(System.getProperty("port", "9999"));

    //等待出库
    private static final int WAIT_OUT=3;
    //取消rfid
    private static final int CANCEL_RFID=4;

    public BztRoadService(NotifyService notifyService, LocalDataService dataService) {
        super(notifyService, dataService);
    }

    @Override
    public void start(Promise<Void> startPromise) {
        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route(CHANGE_USED_CAR).method(HttpMethod.POST).handler(changeUsedCarHandler());

        httpServer.requestHandler(router).listen(PORT, "127.0.0.1", event -> {
            if (event.succeeded()) {
                log.info("web complete of start !");
            } else {
                log.error("web start is fail!");
            }

        });
        startPromise.complete();
    }

    private Handler<RoutingContext> changeUsedCarHandler() {
        return event -> {
            Future<Buffer> body = event.request().body();
            body.onComplete(bodyResult -> {
                JsonObject json = new JsonObject(bodyResult.result());
                GateOutboundInputDTO carDto = json.mapTo(GateOutboundInputDTO.class);
                if (WAIT_OUT == carDto.getStockStatus()) {
                    waitOutMarket(carDto.getMarketCarId());
                }else if(CANCEL_RFID == carDto.getStockStatus()){
                    cancelRfid(carDto.getMarketCarId());
                }
                event.response().end(GSON.toJson(HttpResponse.success()));
            });
            body.onFailure(event1 -> {
                event.response().end(GSON.toJson(HttpResponse.failure()));
                log.error("access change used car is fail! ", event1);
            });
        };
    }


    @Override
    public void inMarket(String carId, TipTextWrapper text) {
        log.info("车辆进场:{}", carId);
        notifyService.notifyCarInMarket(carId,text);
    }

    @Override
    public void outMarket(String carId, TipTextWrapper text) {
        log.info("车辆出场:{}", carId);
        notifyService.notifyCaroutMarket(carId,text);
    }

    @Override
    public void waitOutMarket(String carId) {
        log.info("车辆待出库:{}", carId);
    }

    @Override
    public String bindRfid(String carId,TipTextWrapper text) {
        UUID uuid = UUID.randomUUID();
        log.info("绑定rfid[{}],car:{}", uuid, carId);
        notifyService.notifyCarBindRfid(carId,uuid.toString(),text);
        return uuid.toString();
    }

    @Override
    public void cancelRfid(String carId) {
        log.info("取消rfid,car:{}", carId);
    }

    @Override
    public List<GateCarOutputDTO> searchCarInfo(String vin) {
        return notifyService.searchCar(vin);
    }


    public static void main(String[] args) {
        GateOutboundInputDTO dto = new GateOutboundInputDTO();
        dto.setMarketCarId("123");
        JsonObject json = new JsonObject(GSON.toJson(dto));
        GateOutboundInputDTO dto1 = json.mapTo(GateOutboundInputDTO.class);
        System.out.println();
    }


}
