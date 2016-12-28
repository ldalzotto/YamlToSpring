package com.ldz.view;

import com.ldz.controller.YamlLoadingController;
import com.ldz.model.Operation;
import com.ldz.model.Path;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.SpringNode;
import com.ldz.view.UINodes.YamlNode;
import com.ldz.view.UINodes.generic.AbstractUiNode;
import com.ldz.view.UINodes.generic.UINodePoints;
import com.ldz.view.UINodes.generic.childrenInterface.IHasChildren;
import com.ldz.view.menu.YamlWorkspaceContextMenu;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class YamlToController extends Pane implements IHasChildren<AbstractUiNode>{

    private YamlLoadingController _yamlLoadingController = YamlLoadingController.getInstance();
    private YamlWorkspaceContextMenu _yamlWorkspaceContextMenu = YamlWorkspaceContextMenu.getInstance(this);

    /**
     * This attribute represents all {@link LinkerEventHandler} and their start and end node
     * The order is : Map<{@link LinkerEventHandler}, Map<beginNode, endNode>>
     *     endNode may be null
     *  One {@link LinkerEventHandler} is added for every {@link UINodePoints} output nodes.
     */
    private Map<LinkerEventHandler, Map<Node, Node>> _nodeLinkerEventHandlerMap = new HashMap<LinkerEventHandler, Map<Node, Node>>();

    private static YamlToController _instance = null;

    public static YamlToController getInstance(){
        if(_instance == null){
            _instance = new YamlToController();
            _instance.setId("YamlToController");
        }
        return _instance;
    }

    private YamlToController(){

        setId("YamlToController");

        setBackground(new Background(new BackgroundFill(Color.CADETBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        setPrefSize(200,200);

        addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(event.isSecondaryButtonDown()){
                    System.out.println("Displaying context menu");
                    _yamlWorkspaceContextMenu.show(_instance, event.getScreenX(), event.getScreenY());
                }
            }
        });

    }

    public void createYamlNode(double mouseX, double mouseY, String nodeName){
        System.out.println("Start creating a YAML node...");
        Path nodePath = _yamlLoadingController.getPathFromRessourceName(nodeName);
        Map<String, IYamlDomain> outputData = new HashMap<String, IYamlDomain>();
        if(nodePath != null){
            outputData = _yamlLoadingController.getOperationsFromPath(nodePath);
        }
        YamlNode yamlNode = new YamlNode(mouseX, mouseY, nodeName, outputData, null, Color.RED);

        if(!isAbstractNodeAlreadyPresent(yamlNode)){
            getChildren().add(yamlNode);
            System.out.println("YAML node created with carried information of : " + outputData);
        }

        _nodeLinkerEventHandlerMap.putAll(yamlNode.addLinkerEventHandlerToNode());
    }

    private boolean isAbstractNodeAlreadyPresent(YamlNode yamlNode) {
        boolean isAlreadyPresent = false;
        for(AbstractUiNode abstractUiNode : getChilds()){
            if(abstractUiNode.getId().equals(yamlNode.getId())){
                isAlreadyPresent = true;
            }
        }
        return isAlreadyPresent;
    }

    public void createSpringNode(double mouseX, double mouseY, String nodeName){
        Map<String, IYamlDomain> inputData = new HashMap<String, IYamlDomain>();
        inputData.put("TEST", null);
        SpringNode springNode = new SpringNode(mouseX, mouseY, nodeName, null, inputData, Color.GREEN);
        getChildren().add(springNode);
    }

    public List<AbstractUiNode> getChilds() {
        List<AbstractUiNode> abstractUiNodes = new ArrayList<AbstractUiNode>();
        for (Node node : getChildren()){
            if(node instanceof AbstractUiNode){
                abstractUiNodes.add((AbstractUiNode)node);
            }
        }
        return abstractUiNodes;
    }

    public Map<LinkerEventHandler, Map<Node, Node>> get_nodeLinkerEventHandlerMap() {
        return _nodeLinkerEventHandlerMap;
    }



}
