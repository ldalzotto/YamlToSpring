package com.ldz.view.UINodes;

import com.ldz.model.Operation;
import com.ldz.model.Operations;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.addInputHandler.GenericInputAddableManager;
import com.ldz.view.UINodes.addInputHandler.IGenericInputPointAddableManager;
import com.ldz.view.UINodes.addInputHandler.IIsInputAddable;
import com.ldz.view.linker.LinkerEventHandler;
import com.ldz.view.linker.LinkerEventManager;
import com.ldz.view.workflow.IWorkflowExecution;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * Created by loicd on 02/01/2017.
 */
public class UIListNode extends AbstractUiNode implements IWorkflowExecution<Operation, Operations>, IIsInputAddable<Operation> {

    public UIListNode(double posX, double posY, String nodeName, Map<String, IYamlDomain> outputData,
                      Map<String, IYamlDomain> inputData, Color color) {
        super(posX, posY, nodeName, outputData, inputData, color);
        setId(nodeName);


        displayAbstractNode();

        System.out.println("to List node created.");
    }

    public Operations executeFromInput(List<UINodePoint<Operation>> intputPoints, List<AbstractUiNode> nodeToTransferData) {
        System.out.println("Computing list");
        Operations operations = new Operations();
        List<Operation> operationList = new ArrayList<Operation>();
        for(UINodePoint<Operation> operationUINodePoint : intputPoints){
            operationList.add(operationUINodePoint.get_carriedData().entrySet().iterator().next().getValue());
        }
        operations.set_operations(operationList);

        for(AbstractUiNode abstractUiNode : nodeToTransferData){
            for(UINodePoint uiNodePoint : this.getChilds().get(0).getOutputChildren()){
                for(UINodePoint uiNodePoint1 : abstractUiNode.getChilds().get(0).getChilds()){
                    Iterator<Map.Entry<LinkerEventHandler, Map<Node, Node>>> entryIterator = get_linkerEventHandlerMap().entrySet().iterator();
                    while (entryIterator.hasNext()){
                        Map.Entry<LinkerEventHandler, Map<Node, Node>> linkerEventHandlerMapEntry = entryIterator.next();
                        if (linkerEventHandlerMapEntry.getValue().containsKey(uiNodePoint) && linkerEventHandlerMapEntry.getValue().containsValue(uiNodePoint1)){
                            Map<String, ?> carriedData = uiNodePoint1.get_carriedData();
                            String key = carriedData.keySet().iterator().next();
                            Map<String, IYamlDomain> transitedData = new HashMap<String, IYamlDomain>();
                            transitedData.put(key, operations);
                            uiNodePoint1.set_carriedData(transitedData);
                            System.out.println("TRANSFERING DATA");
                        }
                    }
                }
            }

        }


        return operations;
    }

    public GenericInputAddableManager<Operation> returnInputManager() {
        return new GenericInputAddableManager<Operation>(this, Operation.class);
    }
}
