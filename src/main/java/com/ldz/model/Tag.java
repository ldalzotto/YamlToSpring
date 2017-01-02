package com.ldz.model;


import com.ldz.model.generic.IYamlDomain;

/**
 * Created by ldalzotto on 24/12/2016.
 */
class Tag implements IYamlDomain {
    private String name;
    private String description;
    private ExternalDocumentation externalDocs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExternalDocumentation getExternalDocs() {
        return externalDocs;
    }

    public void setExternalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
    }
}
