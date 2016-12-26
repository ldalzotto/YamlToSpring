package com.ldz.view.UINodes;

import com.ldz.model.Operation;
import com.ldz.model.Path;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.LinkerEventHandler;
import com.ldz.view.UINodes.generic.AbstractUiNode;
import com.ldz.view.UINodes.generic.UINodePoint;
import com.ldz.view.UINodes.generic.UIOutputNodePoints;
import com.ldz.view.YamlToController;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class YamlNode extends AbstractUiNode {

    private Rectangle _rectangle = null;
    private final double MIN_HEIGHT = 100;
    private Text _nodeName = null;

    private YamlNode _instance = null;

    private YamlToController _yamlToController = YamlToController.getInstance();

    private UIOutputNodePoints _output = null;

    public YamlNode(double posX, double posY, String nodeName, Path outputData){
        super();

        _instance = this;

        setLayoutX(posX);
        setLayoutY(posY);

        _rectangle = new Rectangle();
        _rectangle.setVisible(true);
        _rectangle.setFill(Color.RED);

        Map<String, IYamlDomain> carriedOperationData = new HashMap<String, IYamlDomain>();
        try {
            for(Field field : outputData.getClass().getDeclaredFields()){
                field.setAccessible(true);
                if(field.get(outputData)!=null &&
                        (field.get(outputData) instanceof Operation)){
                    carriedOperationData.put(field.getName(),  (Operation)field.get(outputData));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        _output = new UIOutputNodePoints(10.0, carriedOperationData, carriedOperationData);
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
                    //TODO what UInODE is selected here ?
                    updateLinksPosition(_instance);
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

    private void updateLinksPosition(AbstractUiNode abstractUiNode){

         Iterator<Map.Entry<LinkerEventHandler, Map<Node, Node>>> entryIterator = _yamlToController.get_nodeLinkerEventHandlerMap().entrySet().iterator();

        while (entryIterator.hasNext()){
            Map.Entry<LinkerEventHandler, Map<Node, Node>> linkerEventHandlerMapEntry = entryIterator.next();
            for(UIOutputNodePoints uiOutputNodePoints : abstractUiNode.getChilds()){
                for(UINodePoint uiNodePoint : uiOutputNodePoints.getChilds()){

                    //startKey
                    if(linkerEventHandlerMapEntry.getValue().containsKey(uiNodePoint)){
                        linkerEventHandlerMapEntry.getKey().set_startNode(uiNodePoint);
                        linkerEventHandlerMapEntry.getKey().updateStartPosition();
                        //endKey
                    }
                    if (linkerEventHandlerMapEntry.getValue().containsValue(uiNodePoint)) {
                        linkerEventHandlerMapEntry.getKey().set_endNode(uiNodePoint);
                        linkerEventHandlerMapEntry.getKey().updateEndPosition();
                    }
                }
            }
        }

    }

    public Map<LinkerEventHandler, Map<Node, Node>> addLinkerEventHandlerToNode(){
        Map<LinkerEventHandler, Map<Node, Node>> linkEvents = new HashMap<LinkerEventHandler, Map<Node, Node>>();
        for(Node node : _output.getOutputChildren()){
            Map<Node, Node> nodeNodeMap = new HashMap<Node, Node>();
            nodeNodeMap.put(node, null);
            linkEvents.put(new LinkerEventHandler(node), nodeNodeMap);
        }
        return linkEvents;
    }

}
