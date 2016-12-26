package com.ldz.view.UINodes;

import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.AbstractUiNode;
import javafx.scene.paint.Color;

/**
 * Created by ldalzotto on 25/12/2016.
 */
public class SpringNode extends AbstractUiNode {

    public SpringNode(double posX, double posY, String nodeName, IYamlDomain outputData, Color color){
        super(posX, posY, nodeName, outputData, color);
    }

    public void displayNode() {

    }
}
