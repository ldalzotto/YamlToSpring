package com.ldz.model;

/**
 * Created by loicd on 05/01/2017.
 */
public enum  CrudType {
    GET("get"),PUT("put"),POST("post"),DELETE("delete"),
    OPTIONS("options"), HEAD("head"), PATCH("patch");

    private String _name;

    CrudType(String name){
        _name = name;
    }

    public String getValue(){
        return _name;
    }

    public static Boolean contains(String value){
        for(CrudType crudType : CrudType.values()){
            if(crudType.getValue().equals(value)){
                return true;
            }
        }
        return false;
    }

    public static CrudType returnFromValue(String value){
        for(CrudType crudType : CrudType.values()){
            if(crudType.getValue().equals(value)){
                return crudType;
            }
        }
        return null;
    }
}
