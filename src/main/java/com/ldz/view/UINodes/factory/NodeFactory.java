package com.ldz.view.UINodes.factory;

import com.ldz.controller.YamlLoadingController;
import com.ldz.model.Operations;
import com.ldz.model.Path;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.SpringNode;
import com.ldz.view.UINodes.YamlNode;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import com.ldz.view.UINodes.UIListNode;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by loicd on 02/01/2017.
 */
public class NodeFactory {

    private static NodeFactory _instance = null;
    private YamlLoadingController _yamlLoadingController = YamlLoadingController.getInstance();

    private int _nodeListCounter = 0;

    public static NodeFactory getInstance(){
        if(_instance == null){
            _instance = new NodeFactory();
        }
        return _instance;
    }

    public enum NodeType{
        YAML("YAML"), SPRING("SPRING"), LIST("LIST");

        private String name;

        NodeType(String name){
            this.name = name;
        }

        public String getName(){
            return this.name;
        }

    }

    public AbstractUiNode createNode(NodeFactory.NodeType nodeType, double posX, double posY, String nodeName){
        Map<String, IYamlDomain> inputData = null;
        Map<String, IYamlDomain> outputData = null;
        switch (nodeType){
            case YAML:
                System.out.println("Start creating a YAML node...");
                String fullRessourceName = _yamlLoadingController.getFullRessourceName(nodeName);
                Path nodePath = _yamlLoadingController.getPathFromFullRessourceName(fullRessourceName);
                outputData = new HashMap<String, IYamlDomain>();
                if(nodePath != null){
                    outputData = _yamlLoadingController.getOperationsFromPathAndFullRessourceName(nodePath, fullRessourceName);
                }
                return new YamlNode(posX, posY, nodeName, outputData, null, Color.RED);
            case SPRING:
                inputData = new HashMap<String, IYamlDomain>();
                inputData.put(nodeName, new Operations());
                return new SpringNode(posX, posY, nodeName, null, inputData, Color.GREEN);
            case LIST:
                outputData = new HashMap<String, IYamlDomain>();
                outputData.put("Web Services", new Operations());
                _nodeListCounter++;
                return new UIListNode(posX, posY, nodeName + _nodeListCounter, outputData, null, Color.YELLOW);
            default:
                return null;
        }
    }

    public void set_yamlLoadingController(YamlLoadingController _yamlLoadingController) {
        this._yamlLoadingController = _yamlLoadingController;
    }
}
