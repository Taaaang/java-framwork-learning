package per.vertx.learn.server.road;

import per.vertx.learn.data.LocalDataService;
import per.vertx.learn.server.remote.NotifyService;
import per.vertx.learn.server.road.config.HttpRoadConfig;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/14 - 5:07 下午
 **/
public abstract class AbstractHttpRoadService extends AbstractRoadService {

    /**
     * 道闸配置信息
     */
    protected HttpRoadConfig roadConfig;

    public AbstractHttpRoadService(NotifyService notifyService, LocalDataService dataService) {
        super(notifyService, dataService);
    }
}
