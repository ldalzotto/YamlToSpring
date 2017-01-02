package com.ldz.view.UINodes.generic.node;

import com.ldz.constants.UINodePointType;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.IGUIWorkspace;
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
public class UINodePoints extends BorderPane implements IHasOutputAndInputChildren<UINodePoint>, IHasChildren<UINodePoint>,
        IGUIWorkspace {

    private Map<String, IYamlDomain> _carriedData = new HashMap<String, IYamlDomain>();
    private Map<String, IYamlDomain> _inputData = new HashMap<String, IYamlDomain>();
    private UINodeVBox _outputLabelsAndPoints = null;
    private UINodeVBox _inputLabelsAndPoints = null;

    public UINodePoints(double radius, Map<String, IYamlDomain> carriedData){
        super();
        _carriedData = carriedData;
        _outputLabelsAndPoints = new UINodeVBox();
        _outputLabelsAndPoints.setSpacing(5);

        for (Map.Entry<String, IYamlDomain> entry : _carriedData.entrySet()) {
            HashMap<String, IYamlDomain> entryMap = new HashMap<String, IYamlDomain>();
            entryMap.put(entry.getKey(), entry.getValue());

            UINodePointType uiNodePointType;
            if (entry.getValue() == null) {
                uiNodePointType = UINodePointType.DEFAULT;
            } else {
                uiNodePointType = UINodePointType.getValueFromClass(entry.getValue().getClass());
            }

            HBox labelAndPoint = new UINodePoint(entryMap, radius, uiNodePointType);
            _outputLabelsAndPoints.getChildren().add(labelAndPoint);
            System.out.println("Creation of output node " + labelAndPoint.getClass().getSimpleName() + labelAndPoint);
        }

        setRight(_outputLabelsAndPoints);

        setVisible(true);
    }

    public UINodePoints(Map<String, IYamlDomain> carriedData, Map<String, IYamlDomain> inputData){
        super();
        _carriedData = carriedData;
        _inputData = inputData;
        _outputLabelsAndPoints = new UINodeVBox();
        _outputLabelsAndPoints.setSpacing(5);
        _inputLabelsAndPoints = new UINodeVBox();
        _inputLabelsAndPoints.setSpacing(5);

        if(_carriedData != null){
            for (Map.Entry<String, IYamlDomain> entry : _carriedData.entrySet()) {
                HashMap<String, IYamlDomain> entryMap = new HashMap<String, IYamlDomain>();
                entryMap.put(entry.getKey(), entry.getValue());

                UINodePointType uiNodePointType;
                if (entry.getValue() == null) {
                    uiNodePointType = UINodePointType.DEFAULT;
                } else {
                    uiNodePointType = UINodePointType.getValueFromClass(entry.getValue().getClass());
                }

                HBox labelAndPoint = new UINodePoint(entryMap, 10.0, uiNodePointType);
                _outputLabelsAndPoints.getChildren().add(labelAndPoint);
                System.out.println("Creation of output node " + labelAndPoint.getClass().getSimpleName() + labelAndPoint);
            }
        }

        setRight(_outputLabelsAndPoints);

        if(_inputData != null){
            for (Map.Entry<String, IYamlDomain> entry : _inputData.entrySet()) {
                HashMap<String, IYamlDomain> entryMap = new HashMap<String, IYamlDomain>();
                entryMap.put(entry.getKey(), entry.getValue());

                UINodePointType uiNodePointType;
                if (entry.getValue() == null) {
                    uiNodePointType = UINodePointType.DEFAULT;
                } else {
                    uiNodePointType = UINodePointType.getValueFromClass(entry.getValue().getClass());
                }

                HBox labelAndPoint = new UINodePoint(entryMap, 10.0, uiNodePointType);
                _inputLabelsAndPoints.getChildren().add(labelAndPoint);
                System.out.println("Creation of input node " + labelAndPoint.getClass().getSimpleName() + labelAndPoint);
            }
        }


        setLeft(_inputLabelsAndPoints);
        //BorderPane.setAlignment(_outputLabelsAndPoints, Pos.CENTER_RIGHT);

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

    public UINodeVBox get_outputLabelsAndPoints() {
        return _outputLabelsAndPoints;
    }

    public void set_outputLabelsAndPoints(UINodeVBox _outputLabelsAndPoints) {
        this._outputLabelsAndPoints = _outputLabelsAndPoints;
    }

    public UINodeVBox get_inputLabelsAndPoints() {
        return _inputLabelsAndPoints;
    }
}

class UINodeVBox extends VBox implements IGUIWorkspace {

}
