package com.ldz.constants;

import com.ldz.model.Operation;
import com.ldz.model.Operations;
import javafx.scene.paint.Color;

/**
 * Created by ldalzotto on 30/12/2016.
 */
public enum UINodePointType {

    OPERATION(Operation.class, Color.BLUE),
    OPERATIONS(Operations.class, Color.CADETBLUE),
    DEFAULT(Object.class, Color.BLACK);

    private final Class _class;
    private final Color _color;

    UINodePointType(Class classs, Color color){
        _class = classs;
        _color = color;
    }

    private Class get_class() {
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
