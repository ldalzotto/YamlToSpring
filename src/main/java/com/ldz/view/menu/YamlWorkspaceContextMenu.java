package com.ldz.view.menu;

import com.ldz.view.YamlToController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
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

    private final static String MENU_CREATE_PRING_ID = "createSpringNode";

    private MenuItem _createSpringNode = null;

    public static YamlWorkspaceContextMenu getInstance(YamlToController yamlToController){
        _yamlToController = yamlToController;
        if(_instance == null){
            _instance = new YamlWorkspaceContextMenu();
        }
        return _instance;
    }

    private YamlWorkspaceContextMenu(){
        _createSpringNode = new MenuItem("Create Spring node");
        _createSpringNode.setId(MENU_CREATE_PRING_ID);

        getItems().add(_createSpringNode);

        addEventFilter(MouseEvent.MOUSE_EXITED_TARGET, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                System.out.println("Hiding context menu");
                _instance.hide();
            }
        });

        addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(event.isPrimaryButtonDown()){
                    if(event.getPickResult().getIntersectedNode().getId().equals(MENU_CREATE_PRING_ID)){
                        Point2D point2D = _yamlToController.screenToLocal(event.getScreenX(), event.getScreenY());
                        _yamlToController.createSpringNode(point2D.getX(), point2D.getY(), "Spring");
                        System.out.println("Hiding context menu");
                        _instance.hide();
                    }
                }
            }
        });


    }

}
