package per.tomcat.learning.server.servlet;

import per.tomcat.learning.server.http.HttpRequest;
import per.tomcat.learning.server.http.HttpResponse;

/**
 * @author: TangFenQi
 * @description:
 * @date：2021/12/18 13:04
 */
public interface Servlet {

  void init();

  void service(HttpRequest httpRequest, HttpResponse httpResponse);

  void destroy();

}
