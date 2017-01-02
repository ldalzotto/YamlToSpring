package com.ldz.view.UINodes.generic.childrenInterface;

import java.util.List;

/**
 * Created by ldalzotto on 26/12/2016.
 */
public interface IHasChildren<T> {
    List<T> getChilds();
    List<T> getInputChildrens();
    List<T> getOutputChildren();
}
