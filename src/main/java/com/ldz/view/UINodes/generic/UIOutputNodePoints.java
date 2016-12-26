package com.ldz.view.UINodes.generic;

import com.ldz.model.generic.IYamlDomain;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.*;

/**
 * Created by ldalzotto on 25/12/2016.
 */
public class UIOutputNodePoints extends BorderPane{

    private Map<String, IYamlDomain> _carriedData = new HashMap();
    private List<String> _operations = new ArrayList<String>();
    private VBox _labelsAndPoints = null;

    public UIOutputNodePoints(double radius, Map<String, IYamlDomain> carriedData){
        super();
        _carriedData = carriedData;
        _labelsAndPoints = new VBox();
        _labelsAndPoints.setSpacing(5);

        Iterator<Map.Entry<String, IYamlDomain>> carriedDataLabelIterator = _carriedData.entrySet().iterator();
        while (carriedDataLabelIterator.hasNext()){
            Map.Entry<String, IYamlDomain> entry = carriedDataLabelIterator.next();

            //Object cclass = _carriedData.get(keyLabel).get;
            HashMap<String, IYamlDomain> entryMap = new HashMap();
            entryMap.put(entry.getKey(), entry.getValue());

            HBox labelAndPoint = new UINodePoint(entryMap, radius);
            _labelsAndPoints.getChildren().add(labelAndPoint);
        }

        setRight(_labelsAndPoints);
        //BorderPane.setAlignment(_labelsAndPoints, Pos.CENTER_RIGHT);

        setVisible(true);
    }

    public VBox get_labelsAndPoints() {
        return _labelsAndPoints;
    }

    public void set_labelsAndPoints(VBox _labelsAndPoints) {
        this._labelsAndPoints = _labelsAndPoints;
    }
}
