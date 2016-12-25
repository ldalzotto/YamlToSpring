package com.ldz.exception;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class YamlProcessingException extends Exception {

    public YamlProcessingException(String message){
        super(message);
    }

    public YamlProcessingException(String message, Throwable throwable){
        super(message, throwable);
    }

}
