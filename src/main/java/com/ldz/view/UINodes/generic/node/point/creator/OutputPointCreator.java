package com.ldz.view.UINodes.generic.node.point.creator;

import com.ldz.constants.UINodePointType;
import com.ldz.view.UINodes.UINodePoint;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by loicd on 02/01/2017.
 */
public class OutputPointCreator<T> extends AbstractPointCreator<T> implements IPointCreator<T>{

    public OutputPointCreator(Class<T> clazz){
        super(clazz);
    }

    public List<HBox> createLabelAndPoint(Map<String, T> pointsData) {
        List<HBox> labelAndPoints = new ArrayList<HBox>();
        if(pointsData != null){
            for (Map.Entry<String, T> entry : pointsData.entrySet()) {
                HashMap<String, T> entryMap = new HashMap<String, T>();
                entryMap.put(entry.getKey(), entry.getValue());

                UINodePointType uiNodePointType;
                if (entry.getValue() == null) {
                    uiNodePointType = UINodePointType.DEFAULT;
                } else {
                    uiNodePointType = UINodePointType.getValueFromClass(entry.getValue().getClass());
                }

                HBox labelAndPoint = new UINodePoint<T>(entryMap, 10.0, uiNodePointType, true, getClazz());
                labelAndPoints.add(labelAndPoint);
                System.out.println("Creation of output node " + labelAndPoint.getClass().getSimpleName() + labelAndPoint);
            }
        }
        return labelAndPoints;
    }
}
