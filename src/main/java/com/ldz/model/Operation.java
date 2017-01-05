package com.ldz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ldz.model.generic.IYamlDomain;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class Operation implements IYamlDomain {
    private List<String> tags;
    private String summary;
    private String description;
    private ExternalDocumentation externalDocs;
    private String operationId;
    private List<String> consumes;
    private List<String> produces;
    private List<Parameter> parameters;
    private LinkedHashMap<String, Response> responses;
    private List<String> schemes;
    private Boolean deprecated;
    private Object security;

    @JsonIgnore
    private String fullRessourceName;

    @JsonIgnore
    private CrudType _crudType;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public LinkedHashMap<String, Response> getResponses() {
        return responses;
    }

    public void setResponses(LinkedHashMap<String, Response> responses) {
        this.responses = responses;
    }

    public List<String> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<String> schemes) {
        this.schemes = schemes;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public Object getSecurity() {
        return security;
    }

    public void setSecurity(Object security) {
        this.security = security;
    }

    public String getFullRessourceName() {
        return fullRessourceName;
    }

    public void setFullRessourceName(String fullRessourceName) {
        this.fullRessourceName = fullRessourceName;
    }

    public CrudType get_crudType() {
        return _crudType;
    }

    public void set_crudType(CrudType _crudType) {
        this._crudType = _crudType;
    }
}
