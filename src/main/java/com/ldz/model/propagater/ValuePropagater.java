package com.ldz.model.propagater;

import com.ldz.exception.YamlParameterPropagationException;
import com.ldz.model.generic.IYamlDomain;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by loicd on 06/01/2017.
 */
public class ValuePropagater implements IValuePropagater{

    private List<IYamlDomain> _currentValuesToPropagate = new ArrayList<IYamlDomain>();
    private ValuePropagater _oldPropagater = null;

    public List<IYamlDomain> get_currentValuesToPropagate() {
        return _currentValuesToPropagate;
    }

    public ValuePropagater(){
        _oldPropagater = this;
    }

    public void set_currentValuesToPropagate(List<IYamlDomain> _currentValuesToPropagate) {
        this._currentValuesToPropagate = _currentValuesToPropagate;
    }

    public void set_oldPropagater(ValuePropagater _oldPropagater) {
        this._oldPropagater = _oldPropagater;
    }

    public ValuePropagater get_oldPropagater() {
        return _oldPropagater;
    }

    public void propagate(IValuePropagateable classToPropagate) throws YamlParameterPropagationException{
        try {
            Class<?> clazz = classToPropagate.getClass();
            Field[] fields = clazz.getDeclaredFields();
            System.out.println("Start propagating to " + classToPropagate);
            for(Field field : fields){
                PropagateValue propagateValueAnnotation = field.getAnnotation(PropagateValue.class);
                if(propagateValueAnnotation != null){

                    field.setAccessible(true);
                    Object mainFieldobject = null;
                    mainFieldobject = field.get(classToPropagate);

                    /**
                     * Aggregation procedure from old value of propagator
                     */
                    aggregate(classToPropagate, field, propagateValueAnnotation, mainFieldobject);


                    /**
                     * pseudo recursive call to progate value to the deepest
                     */
                    propagateValue(classToPropagate, clazz, propagateValueAnnotation);

                }
            }
        } catch (Exception e) {
            throw new YamlParameterPropagationException(e.getMessage(), e);
        }
    }

    private void propagateValue(IValuePropagateable classToPropagate, Class<?> clazz, PropagateValue propagateValueAnnotation) throws NoSuchFieldException, IllegalAccessException, YamlParameterPropagationException {
        String[] fieldsNameToPropagate = propagateValueAnnotation.fieldsNameToPropagate();
        for(String fieldName : fieldsNameToPropagate){
                Field fieldToPropagate = clazz.getDeclaredField(fieldName);
                fieldToPropagate.setAccessible(true);
                Object fieldObject = fieldToPropagate.get(classToPropagate);


                if(fieldObject instanceof List){
                    ListIterator listIterator = ((List) fieldObject).listIterator();
                    while (listIterator.hasNext()){
                        Object value = listIterator.next();
                        if(value instanceof IValuePropagateable){
                            ValuePropagater valuePropagater = new ValuePropagater();
                            valuePropagater.set_oldPropagater(this);
                            ((IValuePropagateable) value).propagate(valuePropagater);
                        }
                    }
                }else if(fieldObject instanceof Map){
                    Iterator<Map.Entry> mapIterator = ((Map) fieldObject).entrySet().iterator();
                    while (mapIterator.hasNext()){
                        Object currentValue = mapIterator.next().getValue();
                        if(currentValue instanceof IValuePropagateable){
                            ValuePropagater valuePropagater = new ValuePropagater();
                            valuePropagater.set_oldPropagater(this);
                            ((IValuePropagateable) currentValue).propagate(valuePropagater);
                        }
                    }
                }else if(fieldObject instanceof IValuePropagateable){
                    ValuePropagater valuePropagater = new ValuePropagater();
                    valuePropagater.set_oldPropagater(this);
                    ((IValuePropagateable) fieldObject).propagate(valuePropagater);
                }

        }
    }

    private void aggregate(IValuePropagateable classToPropagate, Field field, PropagateValue propagateValueAnnotation, Object mainFieldobject) throws IllegalAccessException {
        if(mainFieldobject instanceof List) {
             ListIterator listIterator = ((List) mainFieldobject).listIterator();
             while (listIterator.hasNext()) {
                 Object value = listIterator.next();

                 //not adding duplicates
                 if (propagateValueAnnotation.classToPropagate().isInstance(value)) {
                     for(IYamlDomain iYamlDomain : _oldPropagater.get_currentValuesToPropagate()){
                         if(!_currentValuesToPropagate.contains(iYamlDomain)){
                             _currentValuesToPropagate.add(iYamlDomain);
                         }
                     }
                     for(Object object : (List)mainFieldobject){
                         if(object instanceof IYamlDomain){
                             if(!_currentValuesToPropagate.contains(object)){
                                 _currentValuesToPropagate.add((IYamlDomain) object);
                             }
                         }
                     }

                     field.set(classToPropagate, _currentValuesToPropagate);
                 }
             }
         }
    }
}
