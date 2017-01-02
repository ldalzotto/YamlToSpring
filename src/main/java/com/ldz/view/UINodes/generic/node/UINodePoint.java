package com.ldz.view.UINodes.generic.node;

import com.ldz.constants.UINodePointType;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.generic.IGUIWorkspace;
import com.ldz.view.UINodes.generic.childrenInterface.IHasChildren;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.*;

/**
 * Created by ldalzotto on 26/12/2016.
 */
public class UINodePoint extends HBox implements IHasChildren<HBox>, IGUIWorkspace {

    private Map<String, IYamlDomain> _carriedData = new HashMap<String, IYamlDomain>();

    public UINodePoint(Map<String, IYamlDomain> carriedData, double radius, UINodePointType uiNodePointType){
        super();
        _carriedData = carriedData;

        Iterator<Map.Entry<String, IYamlDomain>> iterator = carriedData.entrySet().iterator();
        Map.Entry<String, IYamlDomain> entry = iterator.next();

        getChildren().add(new UINodeText(entry.getKey()));

        Circle point = new UINodeCircle(radius);
        point.setFill(uiNodePointType.get_color());
        getChildren().add(point);
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

    public Map<String, IYamlDomain> get_carriedData() {
        return _carriedData;
    }

    public void set_carriedData(Map<String, IYamlDomain> _carriedData) {
        this._carriedData = _carriedData;
    }
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
