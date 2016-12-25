package com.ldz.view;

import com.ldz.controller.YamlLoadingController;
import com.ldz.model.Path;
import com.ldz.view.UINodes.YamlNode;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class YamlToController extends Pane {

    private YamlLoadingController _yamlLoadingController = YamlLoadingController.getInstance();

    private static YamlToController _instance = null;

    private ContextMenu _contextMenu = new ContextMenu();

    public static YamlToController getInstance(){
        if(_instance == null){
            _instance = new YamlToController();
        }
        return _instance;
    }

    private YamlToController(){

        setId("YamlToController");
        MenuItem cut = new MenuItem("Create controller");
        _contextMenu.getItems().add(cut);

        setBackground(new Background(new BackgroundFill(Color.CADETBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        setPrefSize(200,200);
    }

    public void createYamlNode(double mouseX, double mouseY, String nodeName){
        Path nodePath = _yamlLoadingController.getPathFromRessourceName(nodeName);
        YamlNode yamlNode = new YamlNode(mouseX, mouseY, nodeName, nodePath);
        getChildren().add(yamlNode);
    }

}
