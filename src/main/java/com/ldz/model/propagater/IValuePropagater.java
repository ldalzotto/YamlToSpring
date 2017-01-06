package com.ldz.model.propagater;

import com.ldz.exception.YamlParameterPropagationException;

/**
 * Created by loicd on 06/01/2017.
 */
public interface IValuePropagater {
    public void propagate(IValuePropagateable classToPropagate) throws YamlParameterPropagationException;
}
