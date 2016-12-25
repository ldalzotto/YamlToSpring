package com.ldz.view.UINodes;

import com.ldz.view.UINodes.generic.AbstractUiNode;
import com.ldz.view.UINodes.generic.UIOutputNodePoint;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class YamlNode extends AbstractUiNode {

    private Rectangle _rectangle = null;
    private final double MIN_HEIGHT = 100;
    private Text _nodeName = null;

    private UIOutputNodePoint _output = null;

    public YamlNode(double posX, double posY, String nodeName, Object... outputData){
        super();

        setLayoutX(posX);
        setLayoutY(posY);

        _rectangle = new Rectangle();
        _rectangle.setVisible(true);
        _rectangle.setOpacity(0.3);
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
            Circle output2 = new Circle();
            StackPane.setAlignment(_output, Pos.CENTER_RIGHT);
        }

        setVisible(true);
    }

}
