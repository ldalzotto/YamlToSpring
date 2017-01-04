package com.ldz.view.workflow;

import com.ldz.view.UINodes.UINodePoint;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;

import java.util.List;

/**
 * Created by loicd on 02/01/2017.
 */
public interface IWorkflowExecution<I, O> {
    O executeFromInput(List<UINodePoint<I>> intputPoints, List<AbstractUiNode> nodeToTransferData);
    void dataTransfer(List<AbstractUiNode> nodeToTransferData, O outputData);
}
