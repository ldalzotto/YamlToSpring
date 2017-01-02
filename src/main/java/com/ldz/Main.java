package com.ldz;

import com.ldz.view.MainScene;
import com.ldz.view.stages.SpringNodeCreatorStage;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class Main extends Application{

    private final MainScene mainScene = MainScene.getInstance();
    private final SpringNodeCreatorStage _SpringNodeCreatorStage = SpringNodeCreatorStage.getInstance();

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(mainScene);
        _SpringNodeCreatorStage.initOwner(primaryStage);

        primaryStage.show();

        mainScene.initialize();
    }

    public static void main(String[] args){
        Application.launch(args);
    }
}
