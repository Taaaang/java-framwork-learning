package per.vertx.learn.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import per.vertx.learn.data.LocalDataService;
import per.vertx.learn.data.entity.Setting;
import per.vertx.learn.server.road.AbstractRoadService;
import per.vertx.learn.utils.CommonUtils;
import per.vertx.learn.view.wrapper.TipTextWrapper;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/5/12 - 2:46 下午
 **/
public class CenterView {

    private LocalDataService localDataService;
    private AbstractRoadService roadService;

    public CenterView(AbstractRoadService roadService,LocalDataService localDataService) {
        this.localDataService = localDataService;
        this.roadService=roadService;
    }
    private Text info;

    public VBox build(Stage primaryStage){
        VBox centerBox=new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(10);
        Setting setting = localDataService.getSetting();
        info=new Text("市场id【"+setting.getMarketIdDesc()+"】-环境【"+ CommonUtils.getEnvName() +"】");
        Button settingBtn = new Button("设置");
        //绑定rfid
        HBox bindRfidBox = new HBox();
        bindRfidBox.setAlignment(Pos.CENTER);
        bindRfidBox.setSpacing(10);
        Button bindRfidBtn=new Button("绑定rfid");
        TextField bindRfidText = new TextField();
        bindRfidText.setPromptText("请填写carId!");
        bindRfidBox.getChildren().addAll(bindRfidBtn,bindRfidText);
        TipTextWrapper bindRfidTip=new TipTextWrapper();

        //车辆入场
        HBox carEntryBox = new HBox();
        carEntryBox.setAlignment(Pos.CENTER);
        carEntryBox.setSpacing(10);
        Button carEntryBtn=new Button("车辆入场");
        TextField carEntryVin = new TextField();
        carEntryVin.setPromptText("请填写carId!");
        carEntryBox.getChildren().addAll(carEntryBtn,carEntryVin);
        TipTextWrapper carEntryTip=new TipTextWrapper();
        //车辆出场
        HBox carOutBox = new HBox();
        carOutBox.setAlignment(Pos.CENTER);
        carOutBox.setSpacing(10);
        Button carOutBtn=new Button("车辆出场");
        TextField carOutVin = new TextField();
        carOutVin.setPromptText("请填写carId!");
        carOutBox.getChildren().addAll(carOutBtn,carOutVin);
        TipTextWrapper carOutTip=new TipTextWrapper();

        //设置
        settingBtn.setOnAction(event -> {
            new SettingView().build(this,localDataService.getSetting(),localDataService);
        });
        centerBox.getChildren().addAll(info,settingBtn,bindRfidBox,bindRfidTip,carEntryBox,carEntryTip,carOutBox,carOutTip);
        //绑定rfid
        bindRfidBtn.setOnAction(event -> {
            roadService.bindRfid(bindRfidText.getText(),bindRfidTip);
        });
        //车辆进场
        carEntryBtn.setOnAction(event -> {
            roadService.inMarket(carEntryVin.getText(),carEntryTip);
        });
        //车辆出场
        carOutBtn.setOnAction(event -> {
            roadService.outMarket(carOutVin.getText(),carOutTip);
        });



        return centerBox;
    }

    public void rebuildInfo(){
        Setting setting = localDataService.getSetting();
        if(info==null){
            info=new Text("市场id【"+setting.getMarketIdDesc()+"】-环境【"+ CommonUtils.getEnvName() +"】");
        }else {
            info.setText("市场id【"+setting.getMarketIdDesc()+"】-环境【"+ CommonUtils.getEnvName() +"】");
        }
    }

}
