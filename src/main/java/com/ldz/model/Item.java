package com.ldz.model;

import com.ldz.model.generic.IYamlDomain;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class Item extends Schema implements IYamlDomain {

    private String collectionFormat;
    private Boolean uniqueItems;

    public String getCollectionFormat() {
        return collectionFormat;
    }

    public void setCollectionFormat(String collectionFormat) {
        this.collectionFormat = collectionFormat;
    }

    public Boolean getUniqueItems() {
        return uniqueItems;
    }

    public void setUniqueItems(Boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
    }

}
