package com.ldz.model;

import com.ldz.model.generic.IYamlDomain;
import com.ldz.model.propagater.IValuePropagateable;
import com.ldz.model.propagater.IValuePropagater;
import com.ldz.model.propagater.PropagateValue;

import java.util.List;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class Path implements IYamlDomain, IValuePropagateable{
    private Operation get;
    private Operation put;
    private Operation post;
    private Operation delete;
    private Operation options;
    private Operation head;
    private Operation patch;

    @PropagateValue(fieldsNameToPropagate = {"get", "put", "post", "delete", "options", "head", "patch"},
                classToPropagate = Parameter.class)
    private List<Parameter> parameters;

    public Operation getGet() {
        return get;
    }

    public void setGet(Operation get) {
        this.get = get;
    }

    public Operation getPut() {
        return put;
    }

    public void setPut(Operation put) {
        this.put = put;
    }

    public Operation getPost() {
        return post;
    }

    public void setPost(Operation post) {
        this.post = post;
    }

    public Operation getDelete() {
        return delete;
    }

    public void setDelete(Operation delete) {
        this.delete = delete;
    }

    public Operation getOptions() {
        return options;
    }

    public void setOptions(Operation options) {
        this.options = options;
    }

    public Operation getHead() {
        return head;
    }

    public void setHead(Operation head) {
        this.head = head;
    }

    public Operation getPatch() {
        return patch;
    }

    public void setPatch(Operation patch) {
        this.patch = patch;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public void propagate(IValuePropagater iValuePropagater) {
        iValuePropagater.propagate(this);
    }
}
