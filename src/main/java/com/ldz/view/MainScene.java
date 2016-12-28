package com.ldz.view;

import com.ldz.controller.YamlLoadingController;
import com.ldz.exception.YamlProcessingException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

import java.io.File;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class MainScene extends Scene {

    private static MainScene _instance = null;
    private static BorderPane root = new BorderPane();
    private YamlLoadingController _yamlLoadingController = YamlLoadingController.getInstance();
    private YamlFileChooserDialog _yamlFileChooserDialog = YamlFileChooserDialog.getInstance();
    private YamlToController _yamlToController = YamlToController.getInstance();
    private YamlTree _yamlTree = YamlTree.getInstance();

    private MenuBar _menuBar = null;
    private Menu _menuFile = null;

    public MainScene(){
        super(root, 800, 600);
    }

    public static MainScene getInstance(){
        if (_instance == null){
            _instance = new MainScene();
        }
        return _instance;
    }

    public void initialize(){
        _menuBar = new MenuBar();
        _menuFile = new Menu("File");
        _menuFile.setId("fileMenu");

        MenuItem loadFile = new MenuItem("Load YAML file");
        loadFile.setId("loadFileSubMenu");
        loadFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                File yamlFile = _yamlFileChooserDialog.initializeYamlFileChooser();
                try {
                    if(yamlFile != null){
                        _yamlLoadingController.loadingYaml(yamlFile);
                        _yamlTree.initializeYamlTree(_yamlLoadingController.get_swaggerYamlFile());
                    }
                } catch (YamlProcessingException e) {
                    e.printStackTrace();
                }
            }
        });

        _menuFile.getItems().add(loadFile);
        _menuBar.getMenus().addAll(_menuFile);

        root.setTop(_menuBar);

        root.setLeft(_yamlTree);
        _yamlTree.setVisible(false);

        root.setCenter(_yamlToController);
        _yamlToController.setVisible(true);

    }

    public YamlFileChooserDialog get_yamlFileChooserDialog() {
        return _yamlFileChooserDialog;
    }

    public void set_yamlFileChooserDialog(YamlFileChooserDialog _yamlFileChooserDialog) {
        this._yamlFileChooserDialog = _yamlFileChooserDialog;
    }
}
