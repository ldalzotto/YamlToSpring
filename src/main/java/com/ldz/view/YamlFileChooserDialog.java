package com.ldz.view;

import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class YamlFileChooserDialog {

    private static YamlFileChooserDialog _instance = null;
    private final FileChooser _yamlChooser = new FileChooser();

    public static YamlFileChooserDialog getInstance(){
        if(_instance == null){
            _instance = new YamlFileChooserDialog();
        }
        return _instance;
    }

    public File initializeYamlFileChooser(){
        _yamlChooser.setTitle("Choose YAML file");
        _yamlChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("YAML file", "*.yaml")
        );
        return _yamlChooser.showOpenDialog(null);
    }

}
