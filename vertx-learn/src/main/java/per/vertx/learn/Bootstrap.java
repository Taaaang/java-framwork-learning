package per.vertx.learn;

import io.vertx.core.*;
import io.vertx.core.net.ProxyOptions;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import per.vertx.learn.data.LocalDataService;
import per.vertx.learn.data.entity.Setting;
import per.vertx.learn.server.remote.HttpRemoteService;
import per.vertx.learn.server.remote.NotifyService;
import per.vertx.learn.server.road.AbstractRoadService;
import per.vertx.learn.server.road.RoadService;
import per.vertx.learn.server.road.bzt.BztRoadService;
import per.vertx.learn.server.road.config.HttpRoadConfig;
import per.vertx.learn.utils.CommonUtils;
import per.vertx.learn.view.ViewApplication;

import java.time.format.DateTimeFormatter;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/13 - 9:47 上午
 **/
@Slf4j
public class Bootstrap {

    private static DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDataService LOCAL_DATA_SERVICE=new LocalDataService();
    public static NotifyService NOTIFY_SERVICE;
    public static AbstractRoadService ROAD_SERVICE;

    public static void main(String[] args) throws InterruptedException {
        buildEnv();
        Vertx vertx = Vertx.vertx();
        WebClientOptions options=new WebClientOptions();
        options.setProxyOptions(new ProxyOptions());
        NOTIFY_SERVICE=new HttpRemoteService(WebClient.create(vertx,options),new HttpRoadConfig(LOCAL_DATA_SERVICE.getSetting()),LOCAL_DATA_SERVICE);
        ROAD_SERVICE=new BztRoadService(NOTIFY_SERVICE,LOCAL_DATA_SERVICE);


        vertx.deployVerticle(ROAD_SERVICE).onComplete(event -> {
            if(event.succeeded()){
                vertx.executeBlocking(event1 -> {
                    Thread thread = new Thread(() -> Application.launch(ViewApplication.class, args));
                    thread.start();
                });
                log.info("vertical start is success!");
            }else {
                event.cause().printStackTrace();
            }
        });


    }

    private static void buildEnv(){
        if(CommonUtils.getEnv() ==null){
            CommonUtils.setEnv("test");
        }
    }

}
