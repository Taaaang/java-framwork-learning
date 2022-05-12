package per.vertx.learn.view.wrapper;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/5/12 - 3:44 下午
 **/
public class TipTextWrapper extends Text {
    public void buildSuccess(){
        buildSuccess("成功");
    }


    public void buildSuccess(String message){
        this.setFill(Color.GREEN);
        this.setText(message);
    }

    public void buildFailure(String message){
        this.setFill(Color.RED);
        this.setText(message);
    }
}
