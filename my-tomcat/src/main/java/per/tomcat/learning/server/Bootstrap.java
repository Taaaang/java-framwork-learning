package per.tomcat.learning.server;

import per.tomcat.learning.server.http.HttpConnector;

/**
 * @author: TangFenQi
 * @description:
 * @date：2021/12/19 19:03
 */
public class Bootstrap {

  public static void main(String[] args) {
    HttpConnector connector=new HttpConnector(33333);
    connector.start();

  }

}
