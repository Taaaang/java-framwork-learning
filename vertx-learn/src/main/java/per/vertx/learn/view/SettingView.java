package per.vertx.learn.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import per.vertx.learn.data.LocalDataService;
import per.vertx.learn.data.entity.Setting;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/5/12 - 10:53 上午
 **/
public class SettingView {

    public void build(CenterView view,Setting setting, LocalDataService localDataService){

        // 创建新的stage
        Stage newStage = new Stage();
        newStage.setWidth(300);
        newStage.setHeight(200);

        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(5));
        gridpane.setHgap(5);
        gridpane.setVgap(5);
        ColumnConstraints column1 = new ColumnConstraints(100);
        ColumnConstraints column2 = new ColumnConstraints(50, 150, 300);
        column2.setHgrow(Priority.ALWAYS);
        gridpane.getColumnConstraints().addAll(column1, column2);

        Label marketIdLabel = new Label("市场id");
        TextField marketIdText = new TextField();
        marketIdText.setText(setting.getMarketId()==null?"":setting.getMarketId()+"");
        Label appKeyLabel = new Label("appKey");
        TextField appKeyText = new TextField();
        appKeyText.setText(setting.getApplicationKey());
        Label privateKeyLabel = new Label("privateKey");
        TextField privateKeyText = new TextField();
        privateKeyText.setText(setting.getPrivateKey());

        Button saveButt = new Button("保存");
        Button cancelButt = new Button("取消");


        GridPane.setHalignment(marketIdLabel, HPos.RIGHT);
        gridpane.add(marketIdLabel, 0, 0);

        GridPane.setHalignment(marketIdText, HPos.LEFT);
        gridpane.add(marketIdText, 1, 0);

        GridPane.setHalignment(appKeyLabel, HPos.RIGHT);
        gridpane.add(appKeyLabel, 0, 1);

        GridPane.setHalignment(appKeyText, HPos.LEFT);
        gridpane.add(appKeyText, 1, 1);

        GridPane.setHalignment(privateKeyLabel, HPos.RIGHT);
        gridpane.add(privateKeyLabel, 0, 2);

        GridPane.setHalignment(privateKeyText, HPos.LEFT);
        gridpane.add(privateKeyText, 1, 2);

        GridPane.setHalignment(saveButt, HPos.RIGHT);
        GridPane.setHalignment(cancelButt, HPos.RIGHT);
        gridpane.add(saveButt, 0, 3);
        gridpane.add(cancelButt, 1, 3);
        //控制
        cancelButt.setOnAction(event -> {
            newStage.close();
        });
        saveButt.setOnAction(event -> {
            setting.setMarketId(Integer.parseInt(marketIdText.getText()));
            setting.setApplicationKey(appKeyText.getText());
            setting.setPrivateKey(privateKeyText.getText());
            localDataService.setSetting(setting);
            view.rebuildInfo();
            newStage.close();
        });

        Scene scene = new Scene(gridpane, 380, 150, Color.WHITE);
        newStage.setScene(scene);
        // 显示
        newStage.show();

    }





}
