package com.ldz.view.menu;

import com.ldz.view.YamlToController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

/**
 * Created by ldalzotto on 26/12/2016.
 */
public class YamlWorkspaceContextMenu extends ContextMenu {

    private static YamlWorkspaceContextMenu _instance = null;
    //private YamlToController _yamlToController = YamlToController.getInstance();

    private final MenuItem _createSpringNode = new MenuItem("Create Spring node");

    public static YamlWorkspaceContextMenu getInstance(){
        if(_instance == null){
            _instance = new YamlWorkspaceContextMenu();
        }
        return _instance;
    }

    private YamlWorkspaceContextMenu(){

        getItems().add(_createSpringNode);

        addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                System.out.println("Hiding context menu");
                _instance.hide();
            }
        });

        _createSpringNode.setOnMenuValidation(new EventHandler<Event>() {
            public void handle(Event event) {

            }
        });
    }

}
