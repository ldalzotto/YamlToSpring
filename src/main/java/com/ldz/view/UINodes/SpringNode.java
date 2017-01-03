package com.ldz.view.UINodes;

import com.ldz.model.Operations;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import com.ldz.view.workflow.IWorkflowExecution;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by ldalzotto on 25/12/2016.
 */
public class SpringNode extends AbstractUiNode implements IWorkflowExecution<Operations, File>{

    public SpringNode(double posX, double posY, String nodeName, Map<String, IYamlDomain> outputData,
                      Map<String, IYamlDomain> inputData, Color color){
        super(posX, posY, nodeName, outputData, inputData, color);

        setId(nodeName);
        displayAbstractNode();

        System.out.println("Spring node created.");
    }

    public File executeFromInput(List<UINodePoint<Operations>> intputPoints) {
        //generate file
        System.out.println("Generating file");
        return null;
    }
}
