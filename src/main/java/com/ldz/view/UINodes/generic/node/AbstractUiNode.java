package com.ldz.view.UINodes.generic.node;

import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.linker.LinkerEventHandler;
import com.ldz.view.UINodes.UINodePoint;
import com.ldz.view.UINodes.UINodePoints;
import com.ldz.view.UINodes.generic.IGUIWorkspace;
import com.ldz.view.UINodes.generic.childrenInterface.IHasChildren;
import com.ldz.view.UINodes.linker.LinkerEventManager;
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

import java.util.*;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public abstract class AbstractUiNode extends StackPane implements IHasChildren<UINodePoints>, IGUIWorkspace {

    private AbstractUiNode _instance = null;
    private Rectangle _rectangle = null;
    private final double MIN_HEIGHT = 100;
    private Point2D _initialCursorPosition = null;
    private Text _nodeName = null;
    private UINodePoints _output = null;
    private Map<LinkerEventHandler, Map<Node, Node>> _linkerEventHandlerMap = null;
    private final YamlToController _yamlToController = YamlToController.getInstance();
    private final LinkerEventManager _linkerEventManager = LinkerEventManager.getInstance();

    /**
     *
     * @param posX the local X position
     * @param posY the local Y position
     */
    protected AbstractUiNode(double posX, double posY, String nodeName, Map<String, IYamlDomain> outputData,
                             Map<String, IYamlDomain> inputData, Color color){
        super();

        initiateAbstractUINode(posX, posY, nodeName, outputData, inputData, color);

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
                    System.out.println("Start moving node : " + _nodeName);
                    setCursor(Cursor.HAND);
                    _initialCursorPosition = new Point2D(event.getScreenX(), event.getScreenY());
                }
            }
        });

        addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(event.isSecondaryButtonDown()){
                    if((event.getPickResult().getIntersectedNode() instanceof IGUIWorkspace)){
                        if(event.isSecondaryButtonDown()){
                            if(_initialCursorPosition != null){
                                setTranslateX(event.getScreenX() - _initialCursorPosition.getX());
                                setTranslateY(event.getScreenY() - _initialCursorPosition.getY());

                                System.out.println("The linker events associated to " + this + " are moving");
                                updateLinksPosition(_instance);
                            }
                        }
                    } else {
                        System.out.println("Node move not tolerated here");
                    }
                }
            }
        });

        addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                    if(!event.isSecondaryButtonDown()){
                        System.out.println("The mouse has been released, the node " + _nodeName + " has stopped moving.");
                        _initialCursorPosition = null;
                        setLayoutX(getLayoutX() + getTranslateX());
                        setLayoutY(getLayoutY() + getTranslateY());
                        setTranslateX(0);
                        setTranslateY(0);
                    }
                }
        });
    }

    private void initiateAbstractUINode(double posX, double posY, String nodeName, Map<String, IYamlDomain> outputData, Map<String, IYamlDomain> inputData, Color color) {
        _instance = this;

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


        _output = new UINodePoints(outputData, inputData);
        _output.setVisible(true);
        _output.setOpacity(0.3);
    }

    protected void displayAbstractNode(){
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

        addLinkerEventHandlerToNode();
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


    public List<UINodePoints> getInputChildrens() {
        return getChilds();
    }

    public List<UINodePoints> getOutputChildren() {
        return getChilds();
    }

    public void addLinkerEventHandlerToNode(){
        _linkerEventHandlerMap = new HashMap<LinkerEventHandler, Map<Node, Node>>();
        for(Node node : _output.getOutputChildren()){
            Map<Node, Node> nodeNodeMap = new HashMap<Node, Node>();
            nodeNodeMap.put(node, null);
            _linkerEventHandlerMap.put(new LinkerEventHandler(node), nodeNodeMap);
        }
    }

    protected void updateLinksPosition(AbstractUiNode abstractUiNode){

        for (Map.Entry<LinkerEventHandler, Map<Node, Node>> linkerEventHandlerMapEntry : _linkerEventManager.get_nodeLinkerEventHandlerMap().entrySet()) {
            for (UINodePoints uiNodePoints : abstractUiNode.getChilds()) {
                for (UINodePoint uiNodePoint : uiNodePoints.getChilds()) {

                    //startKey
                    //updateing the starts node
                    if (linkerEventHandlerMapEntry.getValue().containsKey(uiNodePoint)) {
                        linkerEventHandlerMapEntry.getKey().set_startNode(uiNodePoint);
                        linkerEventHandlerMapEntry.getKey().updateStartPosition();
                    }
                    //endKey
                    //updating the end nodes
                    if (linkerEventHandlerMapEntry.getValue().containsValue(uiNodePoint)) {
                        linkerEventHandlerMapEntry.getKey().set_endNode(uiNodePoint);
                        linkerEventHandlerMapEntry.getKey().updateEndPosition();
                    }
                }
            }
        }

    }

    public Point2D get_initialCursorPosition() {
        return _initialCursorPosition;
    }

    public Text get_nodeName() {
        return _nodeName;
    }

    public Map<LinkerEventHandler, Map<Node, Node>> get_linkerEventHandlerMap() {
        return _linkerEventHandlerMap;
    }
}
