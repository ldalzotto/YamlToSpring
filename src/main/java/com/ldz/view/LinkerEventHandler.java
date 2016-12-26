package com.ldz.view;

import javafx.event.EventHandler;
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
                Point2D startPoint = _startNode.localToScreen(_startNode.getLayoutX(), _startNode.getLayoutY());
                _line.setStartX(startPoint.getX());
                _line.setStartY(startPoint.getY());
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
        Point2D startPoint = _startNode.localToScreen(_startNode.getLayoutX(), _startNode.getLayoutY());
        _line.setStartX(startPoint.getX());
        _line.setStartY(startPoint.getY());
    }

    public Node get_startNode() {
        return _startNode;
    }

    public void set_startNode(Node _startNode) {
        this._startNode = _startNode;
    }

}
