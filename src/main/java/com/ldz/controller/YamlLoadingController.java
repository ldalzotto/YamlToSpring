package com.ldz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ldz.exception.YamlParameterPropagationException;
import com.ldz.exception.YamlProcessingException;
import com.ldz.model.*;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.model.propagater.ValuePropagater;

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

    public void loadingYaml(File yamlFile) throws YamlProcessingException, YamlParameterPropagationException{
        System.out.println("Loading YAML method called...");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        //Object swagger = null;
        try {
            _swaggerYamlFile = mapper.readValue(yamlFile, SwaggerYamlFile.class);
            System.out.println("YAML loaded successfully !");

            System.out.println("Propagating global parameters from swagger file...");
            _swaggerYamlFile.propagate(new ValuePropagater());
            System.out.println("Propagating global parameters done.");

        } catch (IOException e) {
            throw new YamlProcessingException(e.getMessage(), e.getCause());
        }
    }

    public String getFullRessourceName(String ressourceName){
        if(_swaggerYamlFile != null){
            String ressourceNameTemp = null;
            if(ressourceName.contains(_swaggerYamlFile.getBasePath())){
                ressourceNameTemp = ressourceName.replace(_swaggerYamlFile.getBasePath(), "");
                return ressourceNameTemp;
            }
        }
        return null;
    }

    public Path getPathFromFullRessourceName(String fullRessourceName){
        if (_swaggerYamlFile != null) {
            for (String pathName : _swaggerYamlFile.getPaths().keySet()) {
                if (pathName.equals(fullRessourceName)) {
                    return _swaggerYamlFile.getPaths().get(fullRessourceName);
                }
            }
        }
        return null;
    }

    public Map<String, IYamlDomain> getOperationsFromPathAndFullRessourceName(Path path, String fullRessourceName){
        Map<String, IYamlDomain> stringOperationMap = new HashMap<String, IYamlDomain>();
        try {
            if(path != null){
                for(Field field : path.getClass().getDeclaredFields()){
                    field.setAccessible(true);
                    if(field.get(path)!=null &&
                            (field.get(path) instanceof Operation)){
                        Operation currentOperation = (Operation)field.get(path);
                        currentOperation.setFullRessourceName(fullRessourceName);
                        if(CrudType.contains(field.getName())){
                            currentOperation.set_crudType(CrudType.returnFromValue(field.getName()));
                        }
                        stringOperationMap.put(field.getName(),  currentOperation);
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
