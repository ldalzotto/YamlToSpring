package com.ldz.view.UINodes;

import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.AbstractUiNode;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Created by ldalzotto on 25/12/2016.
 */
public class SpringNode extends AbstractUiNode {

    private SpringNode _instance = null;

    public SpringNode(double posX, double posY, String nodeName, IYamlDomain outputData, Color color){
        super(posX, posY, nodeName, outputData, color);

        _instance = this;

        displayAbstractNode();

        addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(event.isSecondaryButtonDown()){
                    updateLinksPosition(_instance);
                }
            }
        });

        System.out.println("Spring node created.");
    }

    public void displayNode() {

    }
}
