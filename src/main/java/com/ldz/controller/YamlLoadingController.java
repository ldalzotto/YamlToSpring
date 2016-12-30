package com.ldz.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ldz.exception.YamlProcessingException;
import com.ldz.model.*;
import com.ldz.model.generic.IYamlDomain;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class YamlLoadingController {

    private static YamlLoadingController _instance = null;
    private SwaggerYamlFile _swaggerYamlFile = null;

    public static YamlLoadingController getInstance(){
        if(_instance == null){
            _instance = new YamlLoadingController();
        }
        return _instance;
    }

    private YamlLoadingController(){}

    public void loadingYaml(File yamlFile) throws YamlProcessingException{
        System.out.println("Loading YAML method called...");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        //Object swagger = null;
        try {
            _swaggerYamlFile = mapper.readValue(yamlFile, SwaggerYamlFile.class);
            System.out.println("YAML loaded successfully !");

        } catch (IOException e) {
            throw new YamlProcessingException(e.getMessage(), e.getCause());
        }
    }

    public Path getPathFromRessourceName(String ressourceName){
        if (_swaggerYamlFile != null) {
            String ressourceNameTemp = null;
            if(ressourceName.contains(_swaggerYamlFile.getBasePath())){
                ressourceNameTemp = ressourceName.replace(_swaggerYamlFile.getBasePath(), "");
            }
            Iterator<String> ressourceNameIteratore = _swaggerYamlFile.getPaths().keySet().iterator();
            while (ressourceNameIteratore.hasNext()){
                String pathName = ressourceNameIteratore.next();
                if(pathName.equals(ressourceNameTemp)){
                    return _swaggerYamlFile.getPaths().get(ressourceNameTemp);
                }
            }
        }
        return null;
    }

    public Map<String, IYamlDomain> getOperationsFromPath(Path path){
        Map<String, IYamlDomain> stringOperationMap = new HashMap<String, IYamlDomain>();
        try {
            if(path != null){
                for(Field field : path.getClass().getDeclaredFields()){
                    field.setAccessible(true);
                    if(field.get(path)!=null &&
                            (field.get(path) instanceof Operation)){
                        stringOperationMap.put(field.getName(),  (Operation)field.get(path));
                    }
                }
            }
            return stringOperationMap;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return stringOperationMap;
        } catch (NullPointerException e){
            e.printStackTrace();
            return stringOperationMap;
        }
    }

    public SwaggerYamlFile get_swaggerYamlFile() {
        return _swaggerYamlFile;
    }

    public void set_swaggerYamlFile(SwaggerYamlFile _swaggerYamlFile) {
        this._swaggerYamlFile = _swaggerYamlFile;
    }
}
