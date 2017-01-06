package com.ldz.model.propagater;

import com.ldz.model.generic.IYamlDomain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by loicd on 06/01/2017.
 * This annotation is intended to propagate all parameters values from base of swagger object to fields mentionned.
 * Fields type must be of Ma, Array or SimpleObject extending IValuePropagateable
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropagateValue {

    String[] fieldsNameToPropagate() default {};
    Class<? extends IYamlDomain> classToPropagate();

}
