package com.ldz.view.style;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by loicd on 04/01/2017.
 */
public class StyleSheetManager {

    private static StyleSheetManager _instance = null;
    private String _abstractUINodeStye = null;

    private StyleSheetManager(){
        try {
            _abstractUINodeStye = readFile("src/ressources/abstractUINodeStyle.css");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StyleSheetManager getInstance(){
        if(_instance == null){
            _instance = new StyleSheetManager();
        }
        return _instance;
    }

    public String getStyle(StyleNodeType styleNodeType){
        switch (styleNodeType){
            case ABSTRACT_UI_NODE:
                return _abstractUINodeStye;
            default:
                return null;
        }
    }

    private String readFile(String pathname) throws IOException{
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

    public enum StyleNodeType{
        ABSTRACT_UI_NODE;
    }

}
