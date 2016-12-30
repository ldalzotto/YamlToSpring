package com.ldz.view;

import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import com.ldz.view.UINodes.generic.IGUIWorkspace;
import com.ldz.view.UINodes.generic.node.UINodePoint;
import com.ldz.view.UINodes.generic.node.UINodePoints;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.List;
import java.util.Map;

/**
 * Created by ldalzotto on 25/12/2016.
 */
public class LinkerEventHandler implements IGUIWorkspace{

    private YamlToController _yamlToController = YamlToController.getInstance();
    private LinkerEventHandler _instance = null;

    private Node _startNode = null;
    private Node _endNode = null;
    private Line _line = null;

    public LinkerEventHandler(Node startNode){
        _instance = this;
        _startNode = startNode;

        _line = new Line();
        _line.setVisible(false);

        _yamlToController.getChildren().add(_line);

        _startNode.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                System.out.println("Start dragging a linking line");
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
                UINodePoint uiNodePoint = isMouseReleasedOnInputUINode(event);
                if(uiNodePoint == null){
                    _line.setVisible(false);
                    _endNode = null;
                } else {
                    _line.setVisible(true);
                    _endNode = uiNodePoint;
                    _yamlToController.get_nodeLinkerEventHandlerMap().get(_instance)
                            .put(_startNode, _endNode);
                };

            }
        });
    }

    private UINodePoint isMouseReleasedOnInputUINode(MouseEvent event) {
        List<AbstractUiNode> abstractUiNodes = _yamlToController.getChilds();
        for(AbstractUiNode abstractUiNode : abstractUiNodes){
            List<UINodePoints> uiNodePointses = abstractUiNode.getChilds();

            for(UINodePoints uiNodePoints : uiNodePointses){
                //getting input childrens
                List<UINodePoint> inputUiNodePoints = uiNodePoints.getInputChildrens();

                for(UINodePoint uiNodePoint : inputUiNodePoints){
                    Bounds vBoxChildNodeScreen = uiNodePoint.localToScreen(uiNodePoint.getBoundsInLocal());
                    if (vBoxChildNodeScreen.contains(event.getScreenX(), event.getScreenY())) {
                        UINodePoint uiNodePointStart = (UINodePoint) _startNode;
                        //TODO make sure that type are compatibles (lists attribute with new enum ?)
                        Map<String, IYamlDomain> inputMapData = uiNodePointStart.get_carriedData();
                        (uiNodePoint).set_carriedData(inputMapData);
                        System.out.println("Linking data : " + inputMapData.toString() + " from " + uiNodePointStart.toString() + " to "
                                + uiNodePoint.toString());
                        return uiNodePoint;
                    }
                }
            }
        }
        return null;
    }

    public void updateStartPosition(){
        Bounds screenBound = _startNode.localToScreen(_startNode.getBoundsInLocal());
        Bounds pointLocal = _line.screenToLocal(screenBound);
        _line.setStartX(pointLocal.getMaxX());
        _line.setStartY((pointLocal.getMaxY()+pointLocal.getMinY())/2);
    }

    public void updateEndPosition(){
        Bounds screenBound = _endNode.localToScreen(_endNode.getBoundsInLocal());
        Bounds pointLocal = _line.screenToLocal(screenBound);
        _line.setEndX(pointLocal.getMaxX());
        _line.setEndY((pointLocal.getMaxY()+pointLocal.getMinY())/2);
    }

    public Node get_startNode() {
        return _startNode;
    }

    public void set_startNode(Node _startNode) {
        this._startNode = _startNode;
    }

    public Node get_endNode() {
        return _endNode;
    }

    public void set_endNode(Node _endNode) {
        this._endNode = _endNode;
    }

    public Line get_line() {
        return _line;
    }
}
