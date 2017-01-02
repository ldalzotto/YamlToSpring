package com.ldz.view.UINodes.generic.node;

import com.ldz.constants.UINodePointType;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.IGUIWorkspace;
import com.ldz.view.UINodes.generic.childrenInterface.IHasChildren;
import com.ldz.view.UINodes.generic.node.point.creator.IPointCreator;
import com.ldz.view.UINodes.generic.node.point.creator.InputPointCreator;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by loicd on 02/01/2017.
 */
public abstract class AbstractUINodePoints  extends BorderPane {

    public Map<String, IYamlDomain> _outputData = new HashMap<String, IYamlDomain>();
    public Map<String, IYamlDomain> _inputData = new HashMap<String, IYamlDomain>();


    public AbstractUINodePoints(Map<String, IYamlDomain> carriedData, Map<String, IYamlDomain> inputData){
        super();
        _outputData = carriedData;
        _inputData = inputData;
    }

    public List<HBox> createPoints(IPointCreator pointCreator, Map pointsData){
        return pointCreator.createLabelAndPoint(pointsData);
    }

    class UINodeVBox extends VBox implements IGUIWorkspace {

    }
}
