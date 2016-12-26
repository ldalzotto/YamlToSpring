package com.ldz.view.UINodes.generic;

import com.ldz.model.Operation;
import com.ldz.model.Path;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.LinkerEventHandler;
import com.ldz.view.UINodes.generic.childrenInterface.IHasChildren;
import com.ldz.view.YamlToController;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
public abstract class AbstractUiNode extends StackPane implements IHasChildren<UINodePoints> {

    private Rectangle _rectangle = null;
    private final double MIN_HEIGHT = 100;
    private Point2D _initialCursorPosition = null;
    private Text _nodeName = null;
    private UINodePoints _output = null;
    private YamlToController _yamlToController = YamlToController.getInstance();

    public AbstractUiNode(double posX, double posY, String nodeName, IYamlDomain outputData, Color color){
        super();

        setLayoutX(posX);
        setLayoutY(posY);

        _rectangle = new Rectangle();
        _rectangle.setVisible(true);
        _rectangle.setFill(color);


        _nodeName = new Text(nodeName);

        Bounds nameBound = _nodeName.getBoundsInLocal();

        _rectangle.setWidth(nameBound.getWidth());
        if(nameBound.getHeight() < MIN_HEIGHT){
            _rectangle.setHeight(MIN_HEIGHT);
        } else {
            _rectangle.setHeight(nameBound.getHeight());
        }

        Map<String, IYamlDomain> carriedOperationData = new HashMap<String, IYamlDomain>();
        if(outputData instanceof Path){

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
        }



        _output = new UINodePoints(10.0, carriedOperationData, carriedOperationData);
        _output.setVisible(true);
        _output.setOpacity(0.3);



        addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(event.isSecondaryButtonDown()){
                    setCursor(Cursor.OPEN_HAND);
                }
            }
        });

        addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(event.isSecondaryButtonDown()){
                    setCursor(Cursor.HAND);
                    _initialCursorPosition = new Point2D(event.getScreenX(), event.getScreenY());
                }
            }
        });

        addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(event.isSecondaryButtonDown()){
                    if(_initialCursorPosition != null){
                            setTranslateX(event.getScreenX() - _initialCursorPosition.getX());
                            setTranslateY(event.getScreenY() - _initialCursorPosition.getY());
                    }
                }
            }
        });

        addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                    if(!event.isSecondaryButtonDown()){
                        _initialCursorPosition = null;
                        setLayoutX(getLayoutX() + getTranslateX());
                        setLayoutY(getLayoutY() + getTranslateY());
                        setTranslateX(0);
                        setTranslateY(0);
                    }
                }
        });
    }

    public void displayAbstractNode(){
        if(_rectangle != null){
            getChildren().add(_rectangle);
        }
        if(_nodeName != null){
            getChildren().add(_nodeName);
            StackPane.setAlignment(_nodeName, Pos.TOP_CENTER);
        }

        if(_output != null){
            getChildren().add(_output);
            StackPane.setAlignment(_output, Pos.CENTER_LEFT);
        }

        setVisible(true);
    }

    public List<UINodePoints> getChilds(){
        List<UINodePoints> uiNodePointses = new ArrayList<UINodePoints>();
        for (Node node : getChildren()){
            if(node instanceof UINodePoints){
                uiNodePointses.add((UINodePoints)node);
            }
        }
        return uiNodePointses;
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

    public void updateLinksPosition(AbstractUiNode abstractUiNode){

        Iterator<Map.Entry<LinkerEventHandler, Map<Node, Node>>> entryIterator = _yamlToController.get_nodeLinkerEventHandlerMap().entrySet().iterator();

        while (entryIterator.hasNext()){
            Map.Entry<LinkerEventHandler, Map<Node, Node>> linkerEventHandlerMapEntry = entryIterator.next();
            for(UINodePoints uiNodePoints : abstractUiNode.getChilds()){
                for(UINodePoint uiNodePoint : uiNodePoints.getChilds()){

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

}
