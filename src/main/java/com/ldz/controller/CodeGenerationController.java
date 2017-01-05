package com.ldz.controller;

import com.ldz.constants.templates.TemplatesPath;
import com.ldz.constants.templates.TemplatesVariables;
import com.ldz.model.Operation;
import com.ldz.model.Operations;
import com.ldz.model.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by loicd on 05/01/2017.
 */
public class CodeGenerationController {

    private static CodeGenerationController _instance = null;

    private CodeGenerationController(){

    }

    public static CodeGenerationController getinstance(){
        if(_instance == null){
            _instance = new CodeGenerationController();
        }
        return _instance;
    }

    public String generateSpringFilesFromOperations(Operations operations){
        String controller = new String();
        try {
            controller = readFile(TemplatesPath.CONTROLLER_TEMPLATE_PATH);
            //controller.replace("${fullRessourcePath}", operation.getFullRessourceName());
            StringBuilder requestsMapping = new StringBuilder();
            for(Operation operation : operations.get_operations()){
                String requestMapping = processingRequestMapping(operation);
                requestsMapping.append(requestMapping);
            }

            controller = controller.replace(TemplatesVariables.REQUESTS_MAPPING, requestsMapping.toString());

            System.out.println(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return controller;
    }

    private String processingRequestMapping(Operation operation) throws IOException {
        String requestMapping = readFile(TemplatesPath.CONTROLLER_REQUEST_MAPPING_PATH);
        requestMapping = requestMapping.replace(TemplatesVariables.FULL_RESSOURCE_PATH, operation.getFullRessourceName());
        requestMapping = requestMapping.replace(TemplatesVariables.METHOD_NAME, detemineMethodName(operation));
        requestMapping = requestMapping.replace(TemplatesVariables.CAPITAL_CRUDE_OPERATION, operation.get_crudType().getValue().toUpperCase());

        //replacingParameters
        StringBuilder parameters = new StringBuilder();

        List<StringBuilder> parametersDefinition = new ArrayList<StringBuilder>();

        //work with parameters
        for(Parameter parameter : operation.getParameters()){
            StringBuilder parameterDefinition = new StringBuilder();
            //check if the parameter is in path -> add @PathVariable annotation
            Parameter.InValues inValues = Parameter.InValues.getValue(parameter.getIn());
            if(inValues != null){
                //pathvariable
                if(inValues.equals(Parameter.InValues.path)){
                    parameterDefinition.append("@PathVariable ");
                }
            }
            if(parameter.getType() != null){
                parameterDefinition.append(parameter.getType() + " ");
            }
            if(parameter.getName() != null){
                parameterDefinition.append(parameter.getName());
            }
            parametersDefinition.add(parameterDefinition);
        }

        for(int i = 0; i < parametersDefinition.size(); i++){
            parameters.append(parametersDefinition.get(i));
            if(i!=parametersDefinition.size()-1){
                parameters.append(",");
            }
        }
        requestMapping = requestMapping.replace(TemplatesVariables.PARAMETERS, parameters.toString());

        return requestMapping;
    }

    private String readFile(String pathname) throws IOException {
        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int)file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");

        try {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

    private String detemineMethodName(Operation operation){
        StringBuilder methodName = new StringBuilder();
        methodName.append(operation.get_crudType().getValue());

        //traitement du fullressourcename
        String fullRessourceName = operation.getFullRessourceName();
        String[] identifiers = fullRessourceName.split("/");
        for(String s : identifiers){
            StringBuilder stringBuilder = new StringBuilder(s);
            if(!s.isEmpty()){
                if(s.charAt(0) == '{' && s.charAt(s.length()-1) == '}'){
                    stringBuilder.deleteCharAt(0);
                    stringBuilder.deleteCharAt(stringBuilder.length()-1);
                    String upperCaseChar = stringBuilder.substring(0,1).toUpperCase();
                    stringBuilder.deleteCharAt(0);
                    stringBuilder.insert(0,upperCaseChar);
                    stringBuilder.insert(0, "By");
                } else {
                    String upperCaseChar = stringBuilder.substring(0,1).toUpperCase();
                    stringBuilder.deleteCharAt(0);
                    stringBuilder.insert(0, upperCaseChar);
                }

                String parameterName = stringBuilder.toString();
                parameterName = parameterName.replaceAll("[^\\w]", "");

                methodName.append(parameterName);

            }
        }
        return methodName.toString();
    }


}
