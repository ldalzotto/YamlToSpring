package com.ldz.view.menu;

import com.ldz.view.UINodes.factory.NodeFactory;
import com.ldz.view.YamlToController;
import com.ldz.view.stages.SpringNodeCreatorStage;
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

    public final static String MENU_CREATE_PRING_ID = "createSpringNode";
    public final static String TO_LIST_NODE_ID = "createToListNode";

    private final SpringNodeCreatorStage _SpringNodeCreatorStage = SpringNodeCreatorStage.getInstance();


    private MenuItem _createSpringNode = null;
    private MenuItem _createToListNode = null;

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

        _createToListNode = new MenuItem("Create to list node");
        _createToListNode.setId(TO_LIST_NODE_ID);

        getItems().addAll(_createSpringNode, _createToListNode);

        addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                System.out.println("Hiding context menu");
                _instance.hide();
            }
        });

        addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(event.isPrimaryButtonDown()){
                    if(event.getPickResult() != null && event.getPickResult().getIntersectedNode() != null &&
                            event.getPickResult().getIntersectedNode().getId() != null){
                        if(event.getPickResult().getIntersectedNode().getId().equals(MENU_CREATE_PRING_ID)){

                                Point2D point2D = _yamlToController.screenToLocal(event.getScreenX(), event.getScreenY());
                                _SpringNodeCreatorStage.showAndWait();
                                String controllerName = _SpringNodeCreatorStage.get_SpringNodeCreatorScene().get_yamlControllerName().getText();

                                if(_SpringNodeCreatorStage.get_SpringNodeCreatorScene().get_yamlControllerName().getText() != null
                                        && !_SpringNodeCreatorStage.get_SpringNodeCreatorScene().get_yamlControllerName().getText().isEmpty()){
                                    _yamlToController.createUINode(NodeFactory.NodeType.SPRING, point2D.getX(), point2D.getY(), controllerName);
                                } else {
                                    System.out.println("Spring node not created");
                                }

                                System.out.println("Hiding context menu");
                                _instance.hide();
                        } else if(event.getPickResult().getIntersectedNode().getId().equals(TO_LIST_NODE_ID)){
                            Point2D point2D = _yamlToController.screenToLocal(event.getScreenX(), event.getScreenY());

                            //UIListNode uiListNode = new UIListNode();
                            _yamlToController.createUINode(NodeFactory.NodeType.LIST, point2D.getX(), point2D.getY(), "To List Node ");

                            System.out.println("To List node created ");
                            System.out.println("Hiding context menu");
                            _instance.hide();
                        }
                    }
                }
            }
        });




    }

}
