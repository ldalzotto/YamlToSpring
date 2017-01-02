package com.ldz.view.stages;

import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Created by ldalzotto on 29/12/2016.
 */
public class SpringNodeCreatorStage extends Stage {

    private static SpringNodeCreatorStage _instance = null;
    private final SpringNodeCreatorScene _SpringNodeCreatorScene = SpringNodeCreatorScene.getInstance();

    public static SpringNodeCreatorStage getInstance(){
        if(_instance == null){
            _instance = new SpringNodeCreatorStage();
        }
        return _instance;
    }

    public static void refreshInstance(){
        _instance = null;
    }

    private SpringNodeCreatorStage(){
        super();
        this.initModality(Modality.NONE);
        this.setScene(_SpringNodeCreatorScene);
        this.sizeToScene();
        this.hide();

        getScene().getWindow().setOnShown(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                System.out.println("Reseting SpringNodeCreatorStage");
                _SpringNodeCreatorScene.get_yamlControllerName().setText("");
            }
        });
    }

    public SpringNodeCreatorScene get_SpringNodeCreatorScene(){
        return _SpringNodeCreatorScene;
    }
}
