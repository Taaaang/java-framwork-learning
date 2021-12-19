package per.tomcat.learning.client;

/**
 * @author: TangFenQi
 * @description:
 * @date：2021/12/19 19:50
 */
public class ClientStarter {

  public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 10; i++) {
      new Thread(() -> {
        for (int j = 0; j < 10; j++) {
          new Client().connect();
        }
      }).start();
      Thread.sleep(1000L);
    }

  }

}
