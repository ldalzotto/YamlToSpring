package com.ldz.view.UINodes.generic.node;

import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.UINodePoint;
import com.ldz.view.UINodes.generic.IGUIWorkspace;
import com.ldz.view.UINodes.generic.childrenInterface.IHasChildren;
import com.ldz.view.UINodes.generic.node.point.creator.IPointCreator;
import com.ldz.view.UINodes.generic.node.point.creator.InputPointCreator;
import com.ldz.view.UINodes.generic.node.point.creator.OutputPointCreator;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

/**
 * Created by loicd on 02/01/2017.
 */
public abstract class AbstractUINodePoints  extends BorderPane implements IHasChildren<UINodePoint>{

    public Map<String, IYamlDomain> _outputData = new HashMap<String, IYamlDomain>();
    public Map<String, IYamlDomain> _inputData = new HashMap<String, IYamlDomain>();

    public UINodeVBox _outputLabelsAndPoints = null;
    public UINodeVBox _inputLabelsAndPoints = null;

    public AbstractUINodePoints(Map<String, IYamlDomain> carriedData, Map<String, IYamlDomain> inputData){

        _outputData = carriedData;
        _inputData = inputData;

        if(_outputData == null){
            _outputData = new HashMap<String, IYamlDomain>();
        }
        if(_inputData == null){
            _inputData = new HashMap<String, IYamlDomain>();
        }

        _outputLabelsAndPoints = new UINodeVBox();
        _outputLabelsAndPoints.setSpacing(5);
        _inputLabelsAndPoints = new UINodeVBox();
        _inputLabelsAndPoints.setSpacing(5);

        List<HBox> outputPointsAndLabels = createPoints(new OutputPointCreator<IYamlDomain>(IYamlDomain.class), _outputData);
        if(!outputPointsAndLabels.isEmpty()){
            _outputLabelsAndPoints.getChildren().addAll(outputPointsAndLabels);
        }
        setRight(_outputLabelsAndPoints);
        _outputLabelsAndPoints.setVisible(true);

        List<HBox> inputPointsAndLabels = createPoints(new InputPointCreator<IYamlDomain>(IYamlDomain.class), _inputData);
        if(!inputPointsAndLabels.isEmpty()){
            _inputLabelsAndPoints.getChildren().addAll(inputPointsAndLabels);
        }
        setLeft(_inputLabelsAndPoints);
        _inputLabelsAndPoints.setVisible(true);


        setVisible(true);

    }

    public List<HBox> createPoints(IPointCreator pointCreator, Map pointsData){
        return pointCreator.createLabelAndPoint(pointsData);
    }

    public void addInputData(Map<String, IYamlDomain> inputData){
        Iterator<Map.Entry<String, IYamlDomain>> entryIterator = inputData.entrySet().iterator();
        while (entryIterator.hasNext()){
            Map.Entry<String, IYamlDomain> stringIYamlDomainEntry = entryIterator.next();
            _inputData.put(stringIYamlDomainEntry.getKey(), stringIYamlDomainEntry.getValue());
        }

        _inputLabelsAndPoints.getChildren().addAll(createPoints(new InputPointCreator<IYamlDomain>(IYamlDomain.class), inputData));
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
