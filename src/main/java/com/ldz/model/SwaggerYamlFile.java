package com.ldz.model;

import com.ldz.exception.YamlParameterPropagationException;
import com.ldz.model.propagater.IValuePropagater;
import com.ldz.model.propagater.PropagateValue;
import com.ldz.model.propagater.IValuePropagateable;
import com.ldz.model.generic.IYamlDomain;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by ldalzotto on 24/12/2016.
 */
public class SwaggerYamlFile implements IYamlDomain, IValuePropagateable {

    private String swagger;
    private Info info;
    private String host;
    private String basePath;
    private List<String> schemes;
    private List<String> consumes;
    private List<String> produces;
    private LinkedHashMap<String, Path> paths;

    private LinkedHashMap<String, Schema> definitions;

    @PropagateValue(fieldsNameToPropagate = {"paths"},
                classToPropagate = Parameter.class)
    private List<Parameter> parameters;

    private LinkedHashMap<String, Response> responses;
    private Object securityDefinitions;
    private String security;
    private List<Tag> tags;
    private ExternalDocumentation externalDocs;

    public String getSwagger() {
        return swagger;
    }

    public void setSwagger(String swagger) {
        this.swagger = swagger;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public List<String> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<String> schemes) {
        this.schemes = schemes;
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

    public LinkedHashMap<String, Path> getPaths() {
        return paths;
    }

    public void setPaths(LinkedHashMap<String, Path> paths) {
        this.paths = paths;
    }

    public LinkedHashMap<String, Schema> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(LinkedHashMap<String, Schema> definitions) {
        this.definitions = definitions;
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

    public Object getSecurityDefinitions() {
        return securityDefinitions;
    }

    public void setSecurityDefinitions(Object securityDefinitions) {
        this.securityDefinitions = securityDefinitions;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public ExternalDocumentation getExternalDocs() {
        return externalDocs;
    }

    public void setExternalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
    }

    public void propagate(IValuePropagater iValuePropagater) throws YamlParameterPropagationException{
        iValuePropagater.propagate(this);
    }
}
