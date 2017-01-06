package com.ldz.exception;

/**
 * Created by loicd on 06/01/2017.
 */
public class YamlParameterPropagationException extends Exception {

    public YamlParameterPropagationException(String message){
        super(message);
    }

    public YamlParameterPropagationException(String message, Throwable throwable){
        super(message, throwable);
    }

}
