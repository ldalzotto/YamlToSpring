package com.ldz.view.UINodes.generic;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public abstract class AbstractUiNode extends StackPane {

    private Point2D _initialCursorPosition = null;

    public AbstractUiNode(){
        super();

        addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                setCursor(Cursor.OPEN_HAND);
            }
        });

        addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                setCursor(Cursor.HAND);
                _initialCursorPosition = new Point2D(event.getScreenX(), event.getScreenY());
            }
        });

        addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(_initialCursorPosition != null){
                    setTranslateX(event.getScreenX() - _initialCursorPosition.getX());
                    setTranslateY(event.getScreenY() - _initialCursorPosition.getY());
                }
            }
        });

        addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                _initialCursorPosition = null;
                setLayoutX(getLayoutX() + getTranslateX());
                setLayoutY(getLayoutY() + getTranslateY());
                setTranslateX(0);
                setTranslateY(0);
            }
        });
    }

}
