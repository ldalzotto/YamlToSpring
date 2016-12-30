package com.ldz.model;

import com.ldz.model.generic.IYamlDomain;

import java.util.List;

/**
 * Created by ldalzotto on 30/12/2016.
 */
public class Operations implements IYamlDomain{

    private List<Operation> _operations;

    public List<Operation> get_operations() {
        return _operations;
    }

    public void set_operations(List<Operation> _operations) {
        this._operations = _operations;
    }
}
