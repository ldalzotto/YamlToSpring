package com.ldz.model.propagater;

/**
 * Created by loicd on 06/01/2017.
 */
public interface IValuePropagater {
    public void propagate(IValuePropagateable classToPropagate);
}
