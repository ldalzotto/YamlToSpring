package com.ldz.view.UINodes;

import com.ldz.model.generic.IYamlDomain;

/**
 * Created by loicd on 03/01/2017.
 */
public interface IInputPointAddable<T extends IYamlDomain> {
    void manageInputPointCreation();
    Class<T> getIInputPointAddableGenericType();
}
