package com.ldz.view.UINodes.generic.node.point.creator;

import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Map;

/**
 * Created by loicd on 02/01/2017.
 */
public interface IPointCreator<T> {
    List<HBox> createLabelAndPoint(Map<String, T> pointsData);
}
