package com.ldz.view.UINodes.toListNode;

import com.ldz.model.Operation;
import com.ldz.model.Operations;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.UINodePoint;
import com.ldz.view.workflow.IWorkflowExecution;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by loicd on 02/01/2017.
 */
public class UIListNode extends AbstractUiNode implements IWorkflowExecution<Operation, Operations>{

    public UIListNode(double posX, double posY, String nodeName, Map<String, IYamlDomain> outputData,
                      Map<String, IYamlDomain> inputData, Color color) {
        super(posX, posY, nodeName, outputData, inputData, color);
        setId(nodeName);

        //add + button to add additional input node for forming list
        Button addInputButton = new Button();
        addInputButton.setId("addInputListButton");

        addInputButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Map<String, IYamlDomain> newMap = new HashMap<String, IYamlDomain>();
                newMap.put("TEST", new Operation());
                getChilds().get(0).addInputData(newMap);
            }
        });

        displayAbstractNode();
        getChildren().add(addInputButton);
        StackPane.setAlignment(addInputButton, Pos.BOTTOM_LEFT);

        System.out.println("to List node created.");
    }

    public Operations executeFromInput(List<UINodePoint<Operation>> intputPoints) {
        System.out.println("Computing list");
        Operations operations = new Operations();
        List<Operation> operationList = new ArrayList<Operation>();
        for(UINodePoint<Operation> operationUINodePoint : intputPoints){
            operationList.add(operationUINodePoint.get_carriedData().entrySet().iterator().next().getValue());
        }
        operations.set_operations(operationList);
        return operations;
    }
}
