package com.ldz.view.UINodes;

import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import javafx.scene.paint.Color;

import java.util.Map;

/**
 * Created by ldalzotto on 25/12/2016.
 */
public class SpringNode extends AbstractUiNode {

    public SpringNode(double posX, double posY, String nodeName, Map<String, IYamlDomain> outputData,
                      Map<String, IYamlDomain> inputData, Color color){
        super(posX, posY, nodeName, outputData, inputData, color);

        setId(nodeName);
        displayAbstractNode();

        System.out.println("Spring node created.");
    }

}
