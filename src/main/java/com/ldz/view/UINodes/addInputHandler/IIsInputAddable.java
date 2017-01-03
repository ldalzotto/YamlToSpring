package com.ldz.view.UINodes.addInputHandler;

import com.ldz.model.generic.IYamlDomain;

/**
 * Created by loicd on 03/01/2017.
 */
public interface IIsInputAddable<T extends IYamlDomain> {
    GenericInputAddableManager<T> returnInputManager();
}
