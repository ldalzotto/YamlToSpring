package com.ldz.view.UINodes;

import com.ldz.view.LinkerEventHandler;
import com.ldz.view.UINodes.generic.AbstractUiNode;
import com.ldz.view.UINodes.generic.UIOutputNodePoint;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.*;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class YamlNode extends AbstractUiNode {

    private Rectangle _rectangle = null;
    private final double MIN_HEIGHT = 100;
    private Text _nodeName = null;

    private Map<Node, LinkerEventHandler> _linkEvents = new HashMap<Node, LinkerEventHandler>();

    private UIOutputNodePoint _output = null;

    public YamlNode(double posX, double posY, String nodeName, Object... outputData){
        super();

        setLayoutX(posX);
        setLayoutY(posY);

        _rectangle = new Rectangle();
        _rectangle.setVisible(true);
        _rectangle.setFill(Color.RED);

        _output = new UIOutputNodePoint(10.0, outputData);
        _output.setVisible(true);
        _output.setOpacity(0.3);

        _nodeName = new Text(nodeName);

        Bounds nameBound = _nodeName.getBoundsInLocal();
        _rectangle.setWidth(nameBound.getWidth());
        if(nameBound.getHeight() < MIN_HEIGHT){
            _rectangle.setHeight(MIN_HEIGHT);
        } else {
            _rectangle.setHeight(nameBound.getHeight());
        }

        displayNode();

        addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(event.isSecondaryButtonDown()){
                    updateLinksPosition();
                }
            }
        });

        System.out.println("Yaml node created.");
    }

    private void displayNode(){
        if(_rectangle != null){
            getChildren().add(_rectangle);
        }
        if(_nodeName != null){
            getChildren().add(_nodeName);
            StackPane.setAlignment(_nodeName, Pos.TOP_CENTER);
        }
        if(_output != null){
            getChildren().add(_output);
            StackPane.setAlignment(_output, Pos.CENTER_RIGHT);
        }

        setVisible(true);
    }

    private void updateLinksPosition(){
        Iterator<Node> nodeIterator = _linkEvents.keySet().iterator();
        while (nodeIterator.hasNext()){
            Node node = nodeIterator.next();
            _linkEvents.get(node).set_startNode(node);
            _linkEvents.get(node).updateStartPosition();
        }
    }

    public void addLinkerEventHandlerToNode(){
        for(Node node : _output.get_labelsAndPoints().getChildren()){
            _linkEvents.put(node, new LinkerEventHandler(node));
        }
    }

}
