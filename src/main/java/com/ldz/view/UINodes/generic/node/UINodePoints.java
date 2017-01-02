package com.ldz.view.UINodes.generic.node;

import com.ldz.constants.UINodePointType;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.IGUIWorkspace;
import com.ldz.view.UINodes.generic.childrenInterface.IHasChildren;
import com.ldz.view.UINodes.generic.node.point.creator.InputPointCreator;
import com.ldz.view.UINodes.generic.node.point.creator.OutputPointCreator;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

/**
 * Created by ldalzotto on 25/12/2016.
 */
public class UINodePoints extends AbstractUINodePoints implements IHasChildren<UINodePoint>,
        IGUIWorkspace {

    private UINodeVBox _outputLabelsAndPoints = null;
    private UINodeVBox _inputLabelsAndPoints = null;

    public UINodePoints(Map<String, IYamlDomain> outputData, Map<String, IYamlDomain> inputData){
        super(outputData, inputData);
        _outputLabelsAndPoints = new UINodeVBox();
        _outputLabelsAndPoints.setSpacing(5);
        _inputLabelsAndPoints = new UINodeVBox();
        _inputLabelsAndPoints.setSpacing(5);

        List<HBox> outputPointsAndLabels = createPoints(new OutputPointCreator<IYamlDomain>(), _outputData);
        if(!outputPointsAndLabels.isEmpty()){
            _outputLabelsAndPoints.getChildren().addAll(outputPointsAndLabels);
            setRight(_outputLabelsAndPoints);
        }

        List<HBox> inputPointsAndLabels = createPoints(new InputPointCreator<IYamlDomain>(), _inputData);
        if(!inputPointsAndLabels.isEmpty()){
            _inputLabelsAndPoints.getChildren().addAll(inputPointsAndLabels);
            setLeft(_inputLabelsAndPoints);
        }

        setVisible(true);
    }

    public List<UINodePoint> getInputChildrens() {
        List<UINodePoint> uiNodePoints = new ArrayList<UINodePoint>();
        for(Node node : _inputLabelsAndPoints.getChildren()){
            if(node instanceof UINodePoint){
                uiNodePoints.add((UINodePoint)node);
            }
        }
        return uiNodePoints;
    }

    public List<UINodePoint> getOutputChildren() {
        List<UINodePoint> uiNodePoints = new ArrayList<UINodePoint>();
        for(Node node : _outputLabelsAndPoints.getChildren()){
            if(node instanceof UINodePoint){
                uiNodePoints.add((UINodePoint)node);
            }
        }
        return uiNodePoints;
    }

    public List<UINodePoint> getChilds() {
        List<UINodePoint> uiNodePoints = new ArrayList<UINodePoint>();
        uiNodePoints.addAll(getInputChildrens());
        uiNodePoints.addAll(getOutputChildren());
        return uiNodePoints;
    }

    class UINodeVBox extends VBox implements IGUIWorkspace {

    }
}


