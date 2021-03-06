package com.ldz.view;

import com.ldz.model.SwaggerYamlFile;
import com.ldz.view.UINodes.factory.NodeFactory;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

import java.util.Iterator;
import java.util.List;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class YamlTree extends TreeView<String> {

    private static YamlTree _instance = null;
    private TreeItem<String> rootTreeItem = null;

    private final YamlToController _yamlToController = YamlToController.getInstance();

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
                System.out.println("Starting dragging node from YamlTree");
            }
        });
        this.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                _wsSelected = _instance.getSelectionModel().getSelectedItem();
            }
        });
        this.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(_wsSelected != null){
                    System.out.println("Ending dragging node "+ _wsSelected.getValue() +" from YamlTree to workspace");
                }
                List<Node> nodes = getParent().getChildrenUnmodifiable();
                for(Node node : nodes){
                    if(node.getId() != null && node.getId().equals("YamlToController")){
                        if(event.getPickResult().getIntersectedNode() instanceof YamlToController){
                            Point2D nodePoint = _yamlToController.screenToLocal(event.getScreenX(), event.getScreenY());
                            if(_wsSelected != null){
                                _yamlToController.createUINode(NodeFactory.NodeType.YAML, nodePoint.getX(), nodePoint.getY(), _wsSelected.getValue());
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

        for (String path : swaggerYamlFile.getPaths().keySet()) {
            StringBuilder pathStringBuilder = new StringBuilder(path);
            if (basePath != null) {
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
