package com.ldz.view.UINodes.toListNode;

import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.IGUIWorkspace;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import javafx.scene.paint.Color;

import java.util.Map;

/**
 * Created by loicd on 02/01/2017.
 */
public class UIListNode extends AbstractUiNode implements IGUIWorkspace{

    public UIListNode(double posX, double posY, String nodeName, Map<String, IYamlDomain> outputData,
                      Map<String, IYamlDomain> inputData, Color color) {
        super(posX, posY, nodeName, outputData, inputData, color);

        displayAbstractNode();

        System.out.println("to List node created.");
    }

}
