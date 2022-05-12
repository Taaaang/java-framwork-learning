package per.vertx.learn.view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import per.vertx.learn.utils.CommonUtils;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/5/12 - 2:39 下午
 **/
public class TopBarView {

    CenterView centerView;

    public TopBarView(CenterView centerView) {
        this.centerView = centerView;
    }

    public MenuBar build(Stage primaryStage){
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        Menu env=new Menu("环境");
        ToggleGroup envGroup = new ToggleGroup();
        RadioMenuItem testEnv=new RadioMenuItem("测试");
        testEnv.setId("test");
        testEnv.setToggleGroup(envGroup);
        RadioMenuItem preEnv=new RadioMenuItem("预发");
        preEnv.setId("pre");
        preEnv.setToggleGroup(envGroup);
        RadioMenuItem pubEnv=new RadioMenuItem("线上");
        pubEnv.setId("pub");
        pubEnv.setToggleGroup(envGroup);
        env.getItems().addAll(testEnv,preEnv,pubEnv);
        menuBar.getMenus().addAll(env);

        env.setOnAction(event -> {
            if(event.getTarget() instanceof RadioMenuItem){
                RadioMenuItem m=(RadioMenuItem)event.getTarget();
                CommonUtils.setEnv(m.getId());
                centerView.rebuildInfo();
            }
        });

        return menuBar;
    }

}
