package com.ldz.view.UINodes.generic;

import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.childrenInterface.IHasChildren;
import com.ldz.view.UINodes.generic.childrenInterface.IHasOutputAndInputChildren;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

/**
 * Created by ldalzotto on 25/12/2016.
 */
public class UINodePoints extends BorderPane implements IHasOutputAndInputChildren<UINodePoint>, IHasChildren<UINodePoint>{

    private Map<String, IYamlDomain> _carriedData = new HashMap();
    private Map<String, IYamlDomain> _inputData = new HashMap();
    private VBox _outputLabelsAndPoints = null;
    private VBox _inputLabelsAndPoints = null;

    public UINodePoints(double radius, Map<String, IYamlDomain> carriedData){
        super();
        _carriedData = carriedData;
        _outputLabelsAndPoints = new VBox();
        _outputLabelsAndPoints.setSpacing(5);

        Iterator<Map.Entry<String, IYamlDomain>> carriedDataLabelIterator = _carriedData.entrySet().iterator();
        while (carriedDataLabelIterator.hasNext()){
            Map.Entry<String, IYamlDomain> entry = carriedDataLabelIterator.next();

            HashMap<String, IYamlDomain> entryMap = new HashMap();
            entryMap.put(entry.getKey(), entry.getValue());

            HBox labelAndPoint = new UINodePoint(entryMap, radius);
            _outputLabelsAndPoints.getChildren().add(labelAndPoint);
        }

        setRight(_outputLabelsAndPoints);
        //BorderPane.setAlignment(_outputLabelsAndPoints, Pos.CENTER_RIGHT);

        setVisible(true);
    }

    public UINodePoints(double radius, Map<String, IYamlDomain> carriedData, Map<String, IYamlDomain> inputData){
        super();
        _carriedData = carriedData;
        _inputData = inputData;
        _outputLabelsAndPoints = new VBox();
        _outputLabelsAndPoints.setSpacing(5);
        _inputLabelsAndPoints = new VBox();
        _inputLabelsAndPoints.setSpacing(5);

        Iterator<Map.Entry<String, IYamlDomain>> carriedDataLabelIterator = _carriedData.entrySet().iterator();
        while (carriedDataLabelIterator.hasNext()){
            Map.Entry<String, IYamlDomain> entry = carriedDataLabelIterator.next();

            HashMap<String, IYamlDomain> entryMap = new HashMap();
            entryMap.put(entry.getKey(), entry.getValue());

            HBox labelAndPoint = new UINodePoint(entryMap, radius);
            _outputLabelsAndPoints.getChildren().add(labelAndPoint);
        }

        setRight(_outputLabelsAndPoints);

        Iterator<Map.Entry<String, IYamlDomain>> inputDataLabelIterator = _inputData.entrySet().iterator();
        while (inputDataLabelIterator.hasNext()){
            Map.Entry<String, IYamlDomain> entry = inputDataLabelIterator.next();

            HashMap<String, IYamlDomain> entryMap = new HashMap();
            entryMap.put(entry.getKey(), entry.getValue());

            HBox labelAndPoint = new UINodePoint(entryMap, radius);
            _inputLabelsAndPoints.getChildren().add(labelAndPoint);
        }

        setLeft(_inputLabelsAndPoints);
        //BorderPane.setAlignment(_outputLabelsAndPoints, Pos.CENTER_RIGHT);

        setVisible(true);
    }

    public void addInputData(Map<String, IYamlDomain> inputData){
        Iterator<Map.Entry<String, IYamlDomain>> inputDataIterator = inputData.entrySet().iterator();
        while (inputDataIterator.hasNext()){
            Map.Entry<String, IYamlDomain> entry = inputDataIterator.next();
            _inputData.put(entry.getKey(), entry.getValue());
        }
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

    public VBox get_outputLabelsAndPoints() {
        return _outputLabelsAndPoints;
    }

    public void set_outputLabelsAndPoints(VBox _outputLabelsAndPoints) {
        this._outputLabelsAndPoints = _outputLabelsAndPoints;
    }

    public VBox get_inputLabelsAndPoints() {
        return _inputLabelsAndPoints;
    }
}
