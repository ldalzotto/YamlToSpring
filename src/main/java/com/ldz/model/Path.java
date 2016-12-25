package com.ldz.model;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class Path {
    private Operation get;
    private Operation put;
    private Operation post;
    private Operation delete;
    private Operation options;
    private Operation head;
    private Operation patch;
    private String parameters;

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

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
