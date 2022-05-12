package per.vertx.learn.server.road;

import io.vertx.core.AbstractVerticle;
import per.vertx.learn.data.LocalDataService;
import per.vertx.learn.server.road.config.RoadConfig;
import per.vertx.learn.server.remote.NotifyService;

/**
 * 基础的道闸服务
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/14 - 11:17 上午
 **/
public abstract class AbstractRoadService extends AbstractVerticle implements RoadService {

    /**
     * 通知服务
     */
    protected NotifyService notifyService;

    /**
     * 本地数据服务
     */
    protected LocalDataService dataService;

    public AbstractRoadService(NotifyService notifyService, LocalDataService dataService) {
        this.notifyService = notifyService;
        this.dataService = dataService;
    }
}
