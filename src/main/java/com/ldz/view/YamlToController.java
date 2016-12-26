package com.ldz.view;

import com.ldz.controller.YamlLoadingController;
import com.ldz.model.Path;
import com.ldz.view.UINodes.YamlNode;
import com.ldz.view.UINodes.generic.AbstractUiNode;
import com.ldz.view.UINodes.generic.childrenInterface.IHasChildren;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
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

    /**
     * This attribute represents all {@link LinkerEventHandler} and their start and end node
     * The order is : Map<{@link LinkerEventHandler}, Map<beginNode, endNode>>
     *     endNode may be null
     *  One {@link LinkerEventHandler} is added for every {@link com.ldz.view.UINodes.generic.UIOutputNodePoints} output nodes.
     */
    private Map<LinkerEventHandler, Map<Node, Node>> _nodeLinkerEventHandlerMap = new HashMap<LinkerEventHandler, Map<Node, Node>>();

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

        _nodeLinkerEventHandlerMap.putAll(yamlNode.addLinkerEventHandlerToNode());
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
