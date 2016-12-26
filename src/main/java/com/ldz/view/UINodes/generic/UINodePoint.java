package com.ldz.view.UINodes.generic;

import com.ldz.model.generic.IYamlDomain;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ldalzotto on 26/12/2016.
 */
public class UINodePoint extends HBox{

    private Map<String, IYamlDomain> _carriedData = new HashMap<String, IYamlDomain>();

    public UINodePoint(Map<String, IYamlDomain> carriedData, double radius){
        super();
        _carriedData = carriedData;

        Iterator<Map.Entry<String, IYamlDomain>> iterator = carriedData.entrySet().iterator();
        Map.Entry<String, IYamlDomain> entry = iterator.next();

        getChildren().add(new Text(entry.getKey()));

        Circle point = new Circle(radius);
        point.setFill(Color.BLACK);
        getChildren().add(point);
    }

    public Map<String, IYamlDomain> get_carriedData() {
        return _carriedData;
    }

    public void set_carriedData(Map<String, IYamlDomain> _carriedData) {
        this._carriedData = _carriedData;
    }
}
