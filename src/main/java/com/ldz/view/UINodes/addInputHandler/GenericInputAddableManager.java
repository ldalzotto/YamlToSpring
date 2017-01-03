package com.ldz.view.UINodes.addInputHandler;

import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by loicd on 03/01/2017.
 */
public class GenericInputAddableManager<T extends IYamlDomain> implements IGenericInputPointAddableManager {

    private AbstractUiNode _abstractUiNode = null;
    private Class<T> _class = null;

    private int _addCounter;

    public GenericInputAddableManager(AbstractUiNode abstractUiNode, Class<T> classValue){
        _abstractUiNode = abstractUiNode;
        _class = classValue;
        _addCounter = 0;
    }

    public void manageInputPointCreation(){
        //add + button to add additional input node for forming list
        Button addInputButton = new Button();
        addInputButton.setId("addInputListButton");

        addInputButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    Map<String, IYamlDomain> newMap = new HashMap<String, IYamlDomain>();
                    newMap.put(Integer.toString(_addCounter), _class.newInstance());
                    _abstractUiNode.getChilds().get(0).addInputData(newMap);
                    _addCounter ++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        _abstractUiNode.getChildren().add(addInputButton);
        addInputButton.setVisible(true);
        StackPane.setAlignment(addInputButton, Pos.BOTTOM_LEFT);

    }

}
