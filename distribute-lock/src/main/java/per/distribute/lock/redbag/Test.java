package per.distribute.lock.redbag;

import org.redisson.api.RLock;
import per.distribute.lock.redbag.config.JedisConfig;
import per.distribute.lock.redbag.config.RedissonConfig;
import redis.clients.jedis.params.SetParams;

/**
 * @author: TangFenQi
 * @description:
 * @date：2021/12/12 16:10
 */
public class Test {

  public static void main(String[] args) {
    RLock abc = RedissonConfig.getClient().getLock("abc");
    abc.lock();
    JedisConfig.get().del("abc");
    SetParams setParams = new SetParams();
    setParams.nx();
    setParams.ex(30000L);
    String status = JedisConfig.get().set("abc", "1", setParams);
    System.out.println(status);
    String abc1 = JedisConfig.get().set("abc", "1",setParams);
    System.out.println(abc1);
  }

}
