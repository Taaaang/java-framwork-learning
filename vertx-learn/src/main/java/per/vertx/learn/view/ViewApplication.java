package per.vertx.learn.view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Data;
import per.vertx.learn.Bootstrap;
import per.vertx.learn.data.LocalDataService;
import per.vertx.learn.data.entity.Setting;
import per.vertx.learn.server.road.AbstractRoadService;
import per.vertx.learn.server.road.RoadService;
import per.vertx.learn.server.road.bzt.BztRoadService;
import per.vertx.learn.utils.CommonUtils;


/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/15 - 4:01 下午
 **/
@Data
public class ViewApplication extends Application {

    private AbstractRoadService roadService;
    private LocalDataService localDataService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        int width=800;
        int height=400;


        //盒子
        BorderPane borderPane = new BorderPane();

        //组件
        CenterView centerView = new CenterView(roadService,localDataService);
        VBox centerBox=centerView.build(primaryStage);
        MenuBar menuBar = new TopBarView(centerView).build(primaryStage);

        //布局
        borderPane.setTop(menuBar);
        borderPane.setCenter(centerBox);

        primaryStage.setScene(new Scene(borderPane, width, height));
        primaryStage.setTitle("道闸模拟器");
        primaryStage.show();

    }

    @Override
    public void init() throws Exception {
        roadService= Bootstrap.ROAD_SERVICE;
        localDataService=Bootstrap.LOCAL_DATA_SERVICE;
    }
}
