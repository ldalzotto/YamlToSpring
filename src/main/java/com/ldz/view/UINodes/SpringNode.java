package com.ldz.view.UINodes;

import com.ldz.controller.CodeGenerationController;
import com.ldz.model.Operations;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import com.ldz.view.workflow.IWorkflowExecution;
import javafx.scene.paint.Color;
import sun.nio.cs.StandardCharsets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

/**
 * Created by ldalzotto on 25/12/2016.
 */
public class SpringNode extends AbstractUiNode implements IWorkflowExecution<Operations, File>{

    private CodeGenerationController _codeGenerationController = CodeGenerationController.getinstance();

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
        String controller = _codeGenerationController.generateSpringFilesFromOperations(intputPoints.get(0).get_carriedData().entrySet().iterator().next().getValue());
        try {
            Files.write(Paths.get("src/ressources/output/output.txt"), controller.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void dataTransfer(List<AbstractUiNode> nodeToTransferData, File outputData) {}
}
