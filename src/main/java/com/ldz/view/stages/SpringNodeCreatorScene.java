package com.ldz.view.stages;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * Created by ldalzotto on 29/12/2016.
 */
public class SpringNodeCreatorScene extends Scene {

    private static SpringNodeCreatorScene _instance = null;
    public static SpringNodeCreatorScene getInstance(){
        if(_instance == null){
            _instance = new SpringNodeCreatorScene();
        }
        return _instance;
    }

    private static final HBox _enterYamlNameHBox = new HBox();

    private TextField _yamlControllerName = null;
    private Button _yamlNameOkButton = null;

    private SpringNodeCreatorScene(){
        super(_enterYamlNameHBox, 100, 100);
        _yamlControllerName = new TextField();
        _yamlControllerName.setId("yamlControllerName");
        _yamlNameOkButton = new Button("OK");
        _enterYamlNameHBox.getChildren().add(new Text("Enter the Spring controller name : "));
        _enterYamlNameHBox.getChildren().add(_yamlControllerName);
        _enterYamlNameHBox.getChildren().add(_yamlNameOkButton);

        _yamlNameOkButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(event.isPrimaryButtonDown()){
                    if(_yamlControllerName.getText() == null || _yamlControllerName.getText().isEmpty()){
                        System.out.println("Wrong name entered !");
                    } else {
                        event.getPickResult().getIntersectedNode().getScene().getWindow().hide();
                    }
                }
            }
        });
    }

    public TextField get_yamlControllerName(){
        return _yamlControllerName;
    }

    public Button get_yamlNameOkButton() {
        return _yamlNameOkButton;
    }
}
