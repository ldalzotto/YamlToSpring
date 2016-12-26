package com.ldz.view.UINodes.generic;

import com.ldz.model.Operation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ldalzotto on 25/12/2016.
 */
public class UIOutputNodePoint extends BorderPane{

    private Map<String, Object> _carriedData = new HashMap<String, Object>();
    private List<String> _operations = new ArrayList<String>();
    private VBox _labelsAndPoints = null;

    public UIOutputNodePoint(double radius, Object... carriedData){
        super();

        _labelsAndPoints = new VBox();
        _labelsAndPoints.setSpacing(5);

        for(Object object : carriedData){
            _carriedData.put(object.getClass().getSimpleName(), object);
        }

        try {
            for(Field field : _carriedData.get("Path").getClass().getDeclaredFields()){
                field.setAccessible(true);
                if(field.get(_carriedData.get("Path"))!=null &&
                        field.get(_carriedData.get("Path")) instanceof Operation){
                    _carriedData.put(field.getName(), field.get(_carriedData.get("Path")));
                    _operations.add(field.getName());

                    HBox labelAndPoint = new HBox();
                    labelAndPoint.getChildren().add(new Text(field.getName()));

                    Circle point = new Circle(radius);
                    point.setFill(Color.BLACK);
                    labelAndPoint.getChildren().add(point);
                    _labelsAndPoints.getChildren().add(labelAndPoint);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        setRight(_labelsAndPoints);
        //BorderPane.setAlignment(_labelsAndPoints, Pos.CENTER_RIGHT);

        setVisible(true);
    }

    public Map<String, Object> get_carriedData() {
        return _carriedData;
    }

    public void set_carriedData(Map<String, Object> _carriedData) {
        this._carriedData = _carriedData;
    }

    public List<String> get_operations() {
        return _operations;
    }

    public void set_operations(List<String> _operations) {
        this._operations = _operations;
    }

    public VBox get_labelsAndPoints() {
        return _labelsAndPoints;
    }

    public void set_labelsAndPoints(VBox _labelsAndPoints) {
        this._labelsAndPoints = _labelsAndPoints;
    }
}
