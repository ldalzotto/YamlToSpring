package com.ldz.model;

import com.ldz.model.generic.IYamlDomain;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class License implements IYamlDomain {
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
