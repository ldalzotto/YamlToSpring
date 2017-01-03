package com.ldz.view.UINodes;

import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.UINodePoint;
import com.ldz.view.UINodes.generic.IGUIWorkspace;
import com.ldz.view.UINodes.generic.childrenInterface.IHasChildren;
import com.ldz.view.UINodes.generic.node.AbstractUINodePoints;
import com.ldz.view.UINodes.generic.node.point.creator.InputPointCreator;
import com.ldz.view.UINodes.generic.node.point.creator.OutputPointCreator;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

/**
 * Created by ldalzotto on 25/12/2016.
 */
public class UINodePoints extends AbstractUINodePoints implements IGUIWorkspace {

    public UINodePoints(Map<String, IYamlDomain> outputData, Map<String, IYamlDomain> inputData){
        super(outputData, inputData);
    }


}


