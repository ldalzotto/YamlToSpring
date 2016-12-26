package com.ldz.view;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

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
                _line.setEndX(event.getX());
                _line.setEndY(event.getY());
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
