package com.ldz.model;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class Tag {
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
