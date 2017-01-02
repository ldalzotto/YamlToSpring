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

    private YamlNode _instance = null;

    public YamlNode(double posX, double posY, String nodeName, Map<String, IYamlDomain> outputData){
        super(posX, posY, nodeName, outputData, null, Color.RED);

        setId(nodeName);
        _instance = this;

        displayAbstractNode();

        addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(event.isSecondaryButtonDown()){
                    System.out.println("The linker events associated to " + this + " are moving");
                    updateLinksPosition(_instance);
                }
            }
        });

        System.out.println("Yaml node created.");
    }




}
