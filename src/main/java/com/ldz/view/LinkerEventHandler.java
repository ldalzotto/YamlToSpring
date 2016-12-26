package com.ldz.view;

import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.AbstractUiNode;
import com.ldz.view.UINodes.generic.UINodePoint;
import com.ldz.view.UINodes.generic.UIOutputNodePoints;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ldalzotto on 25/12/2016.
 */
public class LinkerEventHandler {

    private YamlToController _yamlToController = YamlToController.getInstance();

    private Node _startNode = null;
    private Line _line = null;

    public LinkerEventHandler(Node startNode){
        _startNode = startNode;

        _line = new Line();
        _line.setVisible(false);

        _yamlToController.getChildren().add(_line);

        _startNode.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Bounds screenBound = _startNode.localToScreen(_startNode.getBoundsInLocal());
                Bounds pointLocal = _line.screenToLocal(screenBound);
                _line.setStartX(pointLocal.getMaxX());
                _line.setStartY((pointLocal.getMaxY()+pointLocal.getMinY())/2);
                _line.setFill(Color.BLACK);
                _line.setVisible(true);
            }
        });

        _startNode.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Point2D lineLocalPoint = _line.screenToLocal(event.getScreenX(), event.getScreenY());
                _line.setEndX(lineLocalPoint.getX());
                _line.setEndY(lineLocalPoint.getY());
            }
        });

        /**
         * Make the information transit
         */
        _startNode.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                for(Node node : _yamlToController.getChildren()){
                    if(node instanceof AbstractUiNode){
                        for(Node childNode : ((AbstractUiNode) node).getChildren()){
                            if(childNode instanceof UIOutputNodePoints){
                                VBox inputData = ((UIOutputNodePoints) childNode).get_inputLabelsAndPoints();
                                if(inputData != null){
                                    for(Node vBoxChildNode : inputData.getChildren()) {
                                        if (vBoxChildNode instanceof UINodePoint) {
                                            Bounds vBoxChildNodeScreen = vBoxChildNode.localToScreen(vBoxChildNode.getBoundsInLocal());
                                            if (vBoxChildNodeScreen.contains(event.getScreenX(), event.getScreenY())) {
                                                UINodePoint uiNodePointStart = (UINodePoint) _startNode;
                                                Map<String, IYamlDomain> inputMapData = uiNodePointStart.get_carriedData();
                                                ((UINodePoint)vBoxChildNode).set_carriedData(inputMapData);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void updateStartPosition(){
        Bounds screenBound = _startNode.localToScreen(_startNode.getBoundsInLocal());
        Bounds pointLocal = _line.screenToLocal(screenBound);
        _line.setStartX(pointLocal.getMaxX());
        _line.setStartY((pointLocal.getMaxY()+pointLocal.getMinY())/2);
    }

    public Node get_startNode() {
        return _startNode;
    }

    public void set_startNode(Node _startNode) {
        this._startNode = _startNode;
    }

}
