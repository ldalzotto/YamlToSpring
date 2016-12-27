package com.ldz.view;

import com.ldz.model.SwaggerYamlFile;
import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.Iterator;
import java.util.List;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class YamlTree extends TreeView<String> {

    private static YamlTree _instance = null;
    private TreeItem<String> rootTreeItem = null;

    private TreeItem<String> _wsSelected = null;

    public static YamlTree getInstance(){
        if(_instance == null){
            _instance = new YamlTree();
            _instance.setId("yamlTree");
        }
        return _instance;
    }

    private YamlTree(){
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                _wsSelected = _instance.getSelectionModel().getSelectedItem();
            }
        });
        this.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                List<Node> nodes = getParent().getChildrenUnmodifiable();
                for(Node node : nodes){
                    if(node.getId() != null && node.getId().equals("YamlToController")){
                        Bounds nodeRect = node.localToScreen(node.getBoundsInLocal());
                        if(nodeRect.contains(event.getScreenX(), event.getScreenY())){
                            YamlToController yamlNode = (YamlToController) node;
                            Point2D nodePoint = node.screenToLocal(event.getScreenX(), event.getSceneY());
                            if(event.getTarget() instanceof LabeledText){
                                yamlNode.createYamlNode(nodePoint.getX(), nodePoint.getY(), ((LabeledText)event.getTarget()).getText());
                            }
                        }
                    }
                }
                _wsSelected = null;
            }
        });
    }

    public void initializeYamlTree(SwaggerYamlFile swaggerYamlFile){
        final String basePath = swaggerYamlFile.getBasePath();
        TreeItem<String> rootTreeItem = new TreeItem<String>("YAML");

        Iterator<String> pathIterator = swaggerYamlFile.getPaths().keySet().iterator();
        while (pathIterator.hasNext()){
            String path = pathIterator.next();
            StringBuilder pathStringBuilder = new StringBuilder(path);
            if(basePath!=null){
                pathStringBuilder.insert(0, basePath);
            }
            rootTreeItem.getChildren().add(new TreeItem<String>(pathStringBuilder.toString()));
        }

        setShowRoot(true);
        setRoot(rootTreeItem);
        setVisible(true);
    }

    public TreeItem<String> getRootTreeItem() {
        return rootTreeItem;
    }

    public void setRootTreeItem(TreeItem<String> rootTreeItem) {
        this.rootTreeItem = rootTreeItem;
    }

    public TreeItem<String> get_wsSelected() {
        return _wsSelected;
    }

    public void set_wsSelected(TreeItem<String> _wsSelected) {
        this._wsSelected = _wsSelected;
    }
}
