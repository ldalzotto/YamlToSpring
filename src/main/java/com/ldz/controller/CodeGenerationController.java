package com.ldz.controller;

import com.ldz.model.Operation;
import com.ldz.model.Operations;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by loicd on 05/01/2017.
 */
public class CodeGenerationController {

    private static CodeGenerationController _instance = null;

    private final String CONTROLLER_TEMPLATE_PATH = "src/ressources/codeTemplate/ControllerTemplate.txt";

    private CodeGenerationController(){

    }

    public static CodeGenerationController getinstance(){
        if(_instance == null){
            _instance = new CodeGenerationController();
        }
        return _instance;
    }

    public File generateSpringFilesFromOperations(Operations operations){
        for(Operation operation : operations.get_operations()){
            try {
                String controller = readFile(CONTROLLER_TEMPLATE_PATH);
                controller.replace("${fullRessourcePath}", operation.getFullRessourceName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
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


}
