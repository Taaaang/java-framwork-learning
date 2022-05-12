package per.vertx.learn.data.entity;

import lombok.Data;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/5/12 - 9:47 上午
 **/
@Data
public class Setting {

    private Integer marketId;

    private String parentPath;

    private String privateKey;

    private String applicationKey;

    public String getMarketIdDesc(){
        return marketId==null?"无":marketId+"";
    }

}
