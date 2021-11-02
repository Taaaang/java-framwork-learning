package per.java.jdbc.standalone;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 定义连接对象的规范
 */
public interface IMyDriver {

    /**
     * 提供连接方法，
     * @param url
     * @return 连接对象
     * @throws SQLException
     */
    Connection connect(String url)
            throws SQLException;

    /**
     * 判断该url是否是连接对象可以处理的
     * @param url
     * @return true：可以连接，false：不可以连接
     * @throws SQLException
     */
    boolean acceptsURL(String url) throws SQLException;

}
