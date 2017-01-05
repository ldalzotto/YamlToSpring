package com.ldz.controller;

import com.ldz.model.Operation;
import com.ldz.model.Operations;
import com.ldz.model.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by loicd on 05/01/2017.
 */
public class CodeGenerationController {

    private static CodeGenerationController _instance = null;

    private final String CONTROLLER_TEMPLATE_PATH = "src/ressources/codeTemplate/controllerTemplate/ControllerTemplate.txt";
    private final String CONTROLLER_REQUEST_MAPPING_PATH = "src/ressources/codeTemplate/controllerTemplate/requestMappingTemplate";

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
            controller = readFile(CONTROLLER_TEMPLATE_PATH);
            //controller.replace("${fullRessourcePath}", operation.getFullRessourceName());
            StringBuilder requestsMapping = new StringBuilder();
            for(Operation operation : operations.get_operations()){
                String requestMapping = processingRequestMapping(operation);
                requestsMapping.append(requestMapping);
            }

            controller = controller.replace("${requestsMapping}", requestsMapping.toString());

            System.out.println(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return controller;
    }

    private String processingRequestMapping(Operation operation) throws IOException {
        String requestMapping = readFile(CONTROLLER_REQUEST_MAPPING_PATH);
        requestMapping = requestMapping.replace("${fullRessourcePath}", operation.getFullRessourceName());
        requestMapping = requestMapping.replace("${methodName}", detemineMethodName(operation));
        requestMapping = requestMapping.replace("${capitalCrudeOperation}", operation.get_crudType().getValue().toUpperCase());

        //replacingParameters
        StringBuilder parameters = new StringBuilder();

        for(Parameter parameter : operation.getParameters()){
            //check if the parameter is in path -> add @PathVariable annotation
        }

        requestMapping = requestMapping.replace("${parameters}", parameters.toString());

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
