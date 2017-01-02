package com.ldz.view.UINodes;

import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Map;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class YamlNode extends AbstractUiNode {

    public YamlNode(double posX, double posY, String nodeName, Map<String, IYamlDomain> outputData, Map<String, IYamlDomain> inputData, Color color){
        super(posX, posY, nodeName, outputData, null, Color.RED);

        setId(nodeName);

        displayAbstractNode();

        System.out.println("Yaml node created.");
    }


}
