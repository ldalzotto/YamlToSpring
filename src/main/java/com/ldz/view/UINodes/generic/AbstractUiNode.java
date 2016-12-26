package com.ldz.view.UINodes.generic;

import com.ldz.view.UINodes.generic.childrenInterface.IHasChildren;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public abstract class AbstractUiNode extends StackPane implements IHasChildren<UIOutputNodePoints> {

    private Point2D _initialCursorPosition = null;

    public AbstractUiNode(){
        super();

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

    public List<UIOutputNodePoints> getChilds(){
        List<UIOutputNodePoints> uiOutputNodePointses = new ArrayList<UIOutputNodePoints>();
        for (Node node : getChildren()){
            if(node instanceof UIOutputNodePoints){
                uiOutputNodePointses.add((UIOutputNodePoints)node);
            }
        }
        return uiOutputNodePointses;
    }

}
