package com.ldz.constants;

import com.ldz.model.Operation;
import com.ldz.model.Operations;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.node.UINodePoints;
import javafx.scene.paint.Color;

/**
 * Created by ldalzotto on 30/12/2016.
 */
public enum UINodePointType {

    OPERATION(Operation.class, Color.BLUE),
    OPERATIONS(Operations.class, Color.CADETBLUE),
    DEFAULT(Object.class, Color.BLACK);

    private Class _class;
    private Color _color;

    private UINodePointType(Class classs, Color color){
        _class = classs;
        _color = color;
    }

    public Class get_class() {
        return _class;
    }

    public Color get_color() {
        return _color;
    }

    public static UINodePointType getValueFromClass(Class classs){
        for(UINodePointType uiNodePointType : UINodePointType.values()){
            if(uiNodePointType.get_class().equals(classs)){
                return uiNodePointType;
            }
        }
        return DEFAULT;
    }
}
