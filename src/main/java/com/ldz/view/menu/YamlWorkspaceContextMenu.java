package com.ldz.view.menu;

import com.ldz.view.YamlToController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

/**
 * Created by ldalzotto on 26/12/2016.
 */
public class YamlWorkspaceContextMenu extends ContextMenu {

    private static YamlWorkspaceContextMenu _instance = null;
    private static YamlToController _yamlToController = null;

    private final MenuItem _createSpringNode = new MenuItem("Create Spring node");

    public static YamlWorkspaceContextMenu getInstance(YamlToController yamlToController){
        _yamlToController = yamlToController;
        if(_instance == null){
            _instance = new YamlWorkspaceContextMenu();
        }
        return _instance;
    }

    private YamlWorkspaceContextMenu(){

        getItems().add(_createSpringNode);

        addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                System.out.println("Hiding context menu");
                Point2D point2D = _yamlToController.screenToLocal(event.getScreenX(), event.getScreenY());
                _yamlToController.createSpringNode(point2D.getX(), point2D.getY(), "Spring");
                _instance.hide();
            }
        });

    }

}
