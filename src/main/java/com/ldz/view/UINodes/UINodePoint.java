package com.ldz.view.UINodes;

import com.ldz.constants.UINodePointType;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.IGUIWorkspace;
import com.ldz.view.UINodes.generic.childrenInterface.IHasChildren;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import com.ldz.view.YamlToController;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by ldalzotto on 26/12/2016.
 */
public class UINodePoint<T> extends HBox implements IHasChildren<HBox>, IGUIWorkspace {

    private Map<String, T> _carriedData = new HashMap<String, T>();
    private Class<T> _clazz = null;

    public UINodePoint(Map<String, T> carriedData, double radius, UINodePointType uiNodePointType, boolean isOutput, Class<T> dataClazz){
        super();
        _clazz = dataClazz;
        _carriedData = carriedData;

        Iterator<Map.Entry<String, T>> iterator = carriedData.entrySet().iterator();
        Map.Entry<String, T> entry = iterator.next();

        if(isOutput){
            getChildren().add(new UINodeText(entry.getKey()));
            Circle point = new UINodeCircle(radius);
            point.setFill(uiNodePointType.get_color());
            getChildren().add(point);
        } else {
            Circle point = new UINodeCircle(radius);
            point.setFill(uiNodePointType.get_color());
            getChildren().add(point);
            getChildren().add(new UINodeText(entry.getKey()));
        }

    }

    public List<HBox> getChilds() {
        List<HBox> hBoxes = new ArrayList<HBox>();
        for (Node node : getChildren()){
            if(node instanceof HBox){
                hBoxes.add((HBox)node);
            }
        }
        return hBoxes;
    }

    public AbstractUiNode retrieveMainNodeParent(){
        Parent currentParent = getParent();
        while (!(currentParent instanceof YamlToController) && !(currentParent instanceof AbstractUiNode)){
            currentParent = currentParent.getParent();
        }
        if(currentParent instanceof YamlToController){
            return null;
        } else if(currentParent instanceof AbstractUiNode){
            return (AbstractUiNode) currentParent;
        } else {
            return null;
        }
    }

    public List<HBox> getInputChildrens() {
        return getChilds();
    }

    public List<HBox> getOutputChildren() {
        return getChilds();
    }

    public Map<String, T> get_carriedData() {
        return _carriedData;
    }

    public void set_carriedData(Map<String, T> _carriedData) {
        this._carriedData = _carriedData;
    }

    public Class<T> get_clazz(){
        return _clazz;
    }

    class UINodeText extends Text implements IGUIWorkspace{

        public UINodeText(String text){
            super(text);
        }
    }

    class UINodeCircle extends Circle implements IGUIWorkspace{

        public UINodeCircle(double radius){
            super(radius);
        }
    }
}

