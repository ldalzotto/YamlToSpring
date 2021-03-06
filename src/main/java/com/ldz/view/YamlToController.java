package com.ldz.view;

import com.ldz.controller.YamlLoadingController;
import com.ldz.view.UINodes.factory.NodeFactory;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import com.ldz.view.UINodes.generic.IGUIWorkspace;
import com.ldz.view.UINodes.generic.childrenInterface.IHasChildren;
import com.ldz.view.linker.LinkerEventManager;
import com.ldz.view.menu.YamlWorkspaceContextMenu;
import com.ldz.view.style.StyleSheetManager;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class YamlToController extends Pane implements IHasChildren<AbstractUiNode>, IGUIWorkspace{

    private YamlLoadingController _yamlLoadingController = YamlLoadingController.getInstance();
    private final YamlWorkspaceContextMenu _yamlWorkspaceContextMenu = YamlWorkspaceContextMenu.getInstance(this);
    private NodeFactory _NodeFactory = NodeFactory.getInstance();
    private final LinkerEventManager _linkerEventManager = LinkerEventManager.getInstance();

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
                    if(event.getPickResult().getIntersectedNode() instanceof YamlToController){
                        System.out.println("Displaying context menu");
                        _yamlWorkspaceContextMenu.show(_instance, event.getScreenX(), event.getScreenY());
                    }
                }
            }
        });

    }

    private boolean isAbstractNodeAlreadyPresent(AbstractUiNode yamlNode) {
        boolean isAlreadyPresent = false;
        for(AbstractUiNode abstractUiNode : getChilds()){
            if(abstractUiNode.getId() != null){
                if(abstractUiNode.getId().equals(yamlNode.getId())){
                    isAlreadyPresent = true;
                }
            }
        }
        return isAlreadyPresent;
    }

    public void createUINode(NodeFactory.NodeType type, double mouseX, double mouseY, String nodeName){
        AbstractUiNode abstractUiNode = _NodeFactory.createNode(type, mouseX, mouseY, nodeName);

        if(!isAbstractNodeAlreadyPresent(abstractUiNode)){
            _linkerEventManager.putLinkers(abstractUiNode.get_linkerEventHandlerMap());
            getChildren().add(abstractUiNode);
            System.out.println( type.getName() + " node created : " + abstractUiNode);
        }

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

    public List<AbstractUiNode> getInputChildrens() {
        return getChilds();
    }

    public List<AbstractUiNode> getOutputChildren() {
        return getChilds();
    }

    public void set_yamlLoadingController(YamlLoadingController _yamlLoadingController) {
        this._yamlLoadingController = _yamlLoadingController;
    }


}
