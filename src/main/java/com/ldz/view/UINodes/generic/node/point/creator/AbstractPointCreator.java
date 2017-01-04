package com.ldz.view.UINodes.generic.node.point.creator;

/**
 * Created by loicd on 04/01/2017.
 */
public abstract class AbstractPointCreator<T> {

    private Class<T> _clazz;

    public AbstractPointCreator(Class<T> clazz){
        _clazz = clazz;
    }

    public Class<T> getClazz(){
        return _clazz;
    }

}
