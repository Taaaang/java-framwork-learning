package per.java.jdbc.standalone;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 驱动管理器，提供统一的获取连接入口
 */
public class MyDriverManager {

    /**
     * 驱动容器，用于存放注册的驱动
     */
    private final static CopyOnWriteArrayList<IMyDriver> registeredDrivers = new CopyOnWriteArrayList<>();

    static {
        //加载引入的驱动
        loadInitialDrivers();
    }

    //禁止创建实例
    private MyDriverManager(){};

    /**
     * 获取连接
     * @param url
     * @return 连接对象
     * @throws SQLException
     */
    public static Connection getConnection(String url) throws SQLException {
        SQLException sqlException=null;
        for (IMyDriver registeredDriver : registeredDrivers) {
            try {
                if(registeredDriver.acceptsURL(url)){
                    return registeredDriver.connect(url);
                }
            } catch (SQLException e) {
                sqlException=e;
            }
        }
        if(sqlException!=null){
            throw sqlException;
        }
        throw new RuntimeException(String.format("no suitable driver found for url:%s!",url));
    }

    /**
     * 注册驱动
     * @param driver 各种厂商的驱动实现
     */
    public static void registerDriver(IMyDriver driver){
        if(driver!=null){
            registeredDrivers.addIfAbsent(driver);
        }else {
            throw new NullPointerException();
        }
    }


    /**
     * 基于SPI机制加载引入的驱动
     */
    private static void loadInitialDrivers(){
        ServiceLoader<IMyDriver> load = ServiceLoader.load(IMyDriver.class);
        Iterator<IMyDriver> iterator = load.iterator();
        while (iterator.hasNext()){
            iterator.next();
        }
    }
}
