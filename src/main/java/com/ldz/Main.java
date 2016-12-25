package com.ldz;

import com.ldz.view.MainScene;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class Main extends Application{

    private MainScene mainScene = MainScene.getInstance();

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(mainScene);
        primaryStage.show();

        mainScene.initialize();

    }

    public static void main(String[] args){
        Application.launch(args);
    }
}
