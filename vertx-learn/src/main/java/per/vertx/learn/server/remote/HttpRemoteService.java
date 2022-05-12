package per.vertx.learn.server.remote;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import per.vertx.learn.data.LocalDataService;
import per.vertx.learn.data.entity.Setting;
import per.vertx.learn.server.remote.entity.client.GateBindInputDTO;
import per.vertx.learn.server.remote.entity.client.GateCarInputDTO;
import per.vertx.learn.server.remote.entity.client.GatePassInputDTO;
import per.vertx.learn.server.remote.entity.client.Result;
import per.vertx.learn.server.road.config.HttpRoadConfig;
import per.vertx.learn.server.remote.entity.GateCarOutputDTO;
import per.vertx.learn.utils.SignatureUtil;
import per.vertx.learn.view.wrapper.TipTextWrapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http协议的远程服务
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/14 - 11:19 上午
 **/
@Slf4j
public class HttpRemoteService implements NotifyService {

    protected WebClient webClient;
    /*
     * 道闸配置信息
     */
    private HttpRoadConfig roadConfig;

    private Gson gson=new Gson();

    private LocalDataService localDataService;

    public HttpRemoteService(WebClient webClient, HttpRoadConfig roadConfig, LocalDataService localDataService) {
        this.webClient=webClient;
        this.roadConfig = roadConfig;
        this.localDataService=localDataService;
    }

    @Override
    public void notifyCarInMarket(String carId, TipTextWrapper text) {
        GatePassInputDTO dto=GatePassInputDTO.entry(carId,roadConfig.getMarketId()+"");
        try {
            Result result = basicRequest(roadConfig.carEntryUrl(), dto, Result.class);
            if(result.isSuccess()){
                log.info("send rfid bind success!");
                text.buildSuccess();
            }else {
                log.error("send rfid bind failed!");
                text.buildFailure(result.getMsg());
            }
        }catch (Exception ex){
            log.error("异常",ex);
            text.buildFailure(ex.getMessage());
        }

    }



    @Override
    public void notifyCaroutMarket(String carId, TipTextWrapper text) {
        GatePassInputDTO dto=GatePassInputDTO.out(carId,roadConfig.getMarketId()+"");
        try {
            Result result = basicRequest(roadConfig.carOutUrl(), dto, Result.class);
            if(result.isSuccess()){
                log.info("send rfid bind success!");
                text.buildSuccess();
            }else {
                log.error("send rfid bind failed!");
                text.buildFailure(result.getMsg());
            }
        }catch (Exception ex){
            log.error("异常",ex);
            text.buildFailure(ex.getMessage());
        }
    }

    @Override
    public void notifyCarBindRfid(String carId,String rfid,TipTextWrapper text) {
        GateBindInputDTO bindInputDTO=new GateBindInputDTO();
        bindInputDTO.setMarketCarId(carId);
        bindInputDTO.setRfid(rfid);
        bindInputDTO.setMarketCode(roadConfig.getMarketId()+"");

        try {
            Result result = basicRequest(roadConfig.bindRfidUrl(), bindInputDTO, Result.class);
            if(result.isSuccess()){
                log.info("send rfid bind success!");
                text.buildSuccess();
            }else {
                log.error("send rfid bind failed!");
                text.buildFailure(result.getMsg());
            }
        }catch (Exception ex){
            log.error("异常",ex);
            text.buildFailure(ex.getMessage());
        }
    }

    @Override
    public List<GateCarOutputDTO> searchCar(String vin) {
        Setting setting = localDataService.getSetting();
        GateCarInputDTO carInputDTO=new GateCarInputDTO(vin,roadConfig.getMarketId()+"");
        cn.hutool.http.HttpResponse response = HttpUtil.createPost(roadConfig.getCarByVinUrl())
                .header("app-key", setting.getApplicationKey())
                .header("sign", SignatureUtil.getSignature(convertToMap(carInputDTO), setting.getPrivateKey()))
                .body(gson.toJson(carInputDTO))
                .execute();
        JsonObject jsonObject=new JsonObject(response.body());
        if(jsonObject.getString("code").equals(Result.SUCCESS_CODE)){
            return gson.fromJson(jsonObject.getString("data"), new TypeToken<List<GateCarOutputDTO>>() {
            }.getType());
        }else {
            log.error("send searchCar failed!");
            throw new RuntimeException("send searchCar failed!");
        }
    }

    private <K> K basicRequest(String url,Object object,Class<K> clazz){
        Setting setting = localDataService.getSetting();
        Map<String,String> headers=new HashMap<>();
        headers.put("app-key",setting.getApplicationKey());
        headers.put("sign",SignatureUtil.getSignature(convertToMap(object),setting.getPrivateKey()));
        cn.hutool.http.HttpRequest httpRequest = HttpUtil.createPost(url).body(gson.toJson(object)).
                addHeaders(headers);
        cn.hutool.http.HttpResponse execute = httpRequest.execute();
        return gson.fromJson(execute.body(),clazz);
    }

    private Map<String,String> convertToMap(Object object){
        Field[] declaredFields = object.getClass().getDeclaredFields();
        HashMap<String,String> hashMap=new HashMap<>(declaredFields.length);
        try {
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                Object o = declaredField.get(object);
                Class<?> type = declaredField.getType();
                if (type.equals(String.class)) {
                    hashMap.put(declaredField.getName(), (String) o);
                }
                if(type.equals(Integer.class)||type.equals(int.class)){
                    hashMap.put(declaredField.getName(),Integer.toString(((int)o)));
                }
                if(type.equals(Long.class)||type.equals(long.class)){
                    hashMap.put(declaredField.getName(),Long.toString(((long)o)));
                }
            }
        }catch (Exception ex){
            log.error("convert failed from object to map! ",ex);
            throw new RuntimeException("convert error!");
        }
        return hashMap;
    }


}
