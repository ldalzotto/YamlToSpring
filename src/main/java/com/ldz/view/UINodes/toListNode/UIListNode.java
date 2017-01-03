package com.ldz.view.UINodes.toListNode;

import com.ldz.model.Operation;
import com.ldz.model.Operations;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.UINodePoint;
import com.ldz.view.UINodes.generic.IWorkflowExecution;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import javafx.scene.paint.Color;

import java.util.ArrayList;
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
        displayAbstractNode();

        System.out.println("to List node created.");
    }

    public Operations executeFromInput(List<UINodePoint<Operation>> intputPoints) {
        Operations operations = new Operations();
        List<Operation> operationList = new ArrayList<Operation>();
        for(UINodePoint<Operation> operationUINodePoint : intputPoints){
            operationList.add(operationUINodePoint.get_carriedData().entrySet().iterator().next().getValue());
        }
        operations.set_operations(operationList);
        return operations;
    }
}
