package com.ldz.view.workflow;

import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.UINodePoint;

import java.util.List;

/**
 * Created by loicd on 02/01/2017.
 */
public interface IWorkflowExecution<I, O> {
    O executeFromInput(List<UINodePoint<I>> intputPoints);
}
