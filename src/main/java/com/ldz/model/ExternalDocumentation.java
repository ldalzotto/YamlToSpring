package com.ldz.model;

import com.ldz.model.generic.IYamlDomain;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class ExternalDocumentation implements IYamlDomain {
    private String description;
    private String url;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
