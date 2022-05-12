package per.vertx.learn.data;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import per.vertx.learn.data.entity.Setting;
import per.vertx.learn.utils.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/14 - 11:26 上午
 **/
@Slf4j
public class LocalDataService {

    private static String FILE_SAVE_PATH="/leveldb/gate_db";

    private static Gson GSON=new Gson();

    private static Setting SETTING;

    private static String KEY_PREFIX="setting-";

    public Setting getSetting() {

        Options options = new Options();
        options.createIfMissing(true);
        DB db=null;
        try {
            db = factory.open(new File(getFilePath()), options);
            System.out.println(getFilePath());
            String settingStr = asString(db.get(bytes(getSettingKey())));
            System.out.println();
            Setting setting = GSON.fromJson(settingStr, Setting.class);
            setting=setting==null?new Setting():setting;
            setting.setParentPath(getParentUrl());
            return setting;
        } catch (IOException e) {
            log.error("打开数据库失败！",e);
            throw new RuntimeException(e);
        } finally {
            // Make sure you close the db to shutdown the
            // database and avoid resource leaks.
            if(db!=null) {
                try {
                    db.close();
                } catch (IOException e) {
                    log.error("关闭数据库失败！",e);
                }
            }
        }
    }

    public void setSetting(Setting setting){
        Options options = new Options();
        options.createIfMissing(true);
        DB db=null;
        try {
            db = factory.open(new File(getFilePath()), options);
            db.put(bytes(getSettingKey()),bytes(GSON.toJson(setting)));
        } catch (IOException e) {
            log.error("打开数据库失败！",e);
            throw new RuntimeException(e);
        } finally {
            // Make sure you close the db to shutdown the
            // database and avoid resource leaks.
            if(db!=null) {
                try {
                    db.close();
                } catch (IOException e) {
                    log.error("关闭数据库失败！");
                }
            }
        }
    }

    private static String PARENT_URL_TEST="http://danube-usedcar-market.stable.dasouche.net/gate/bzt";
    private static String PARENT_URL_PRE="http://danube-usedcar-market.prepub.souche.com/gate/bzt";
    private static String PARENT_URL_PUB="https://danube-usedcar-market.souche.com/gate/bzt";

    private String getParentUrl(){
        String env = CommonUtils.getEnv();
        switch (env){
            case "test":
                return PARENT_URL_TEST;
            case "pre":
                return PARENT_URL_PRE;
            case "pub":
                return PARENT_URL_PUB;
            default:
                throw new RuntimeException("环境配置异常！未知环境："+env);
        }
    }

    private String getSettingKey(){
        return KEY_PREFIX+CommonUtils.getEnv();
    }

    private String getFilePath(){
        String path = System.getProperty("user.dir");
        return path+FILE_SAVE_PATH;
    }

    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir");
        System.out.println(path+FILE_SAVE_PATH);
    }

}
