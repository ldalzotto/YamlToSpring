package com.ldz.model;

import com.ldz.model.generic.IYamlDomain;

import java.util.LinkedHashMap;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class Response implements IYamlDomain {
    private String description;
    private Schema schema;
    private LinkedHashMap<String, Header> headers;
    private Object examples;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public LinkedHashMap<String, Header> getHeaders() {
        return headers;
    }

    public void setHeaders(LinkedHashMap<String, Header> headers) {
        this.headers = headers;
    }

    public Object getExamples() {
        return examples;
    }

    public void setExamples(Object examples) {
        this.examples = examples;
    }
}
