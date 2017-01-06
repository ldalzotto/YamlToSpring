package com.ldz.model.propagater;

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

    public void set_currentValuesToPropagate(List<IYamlDomain> _currentValuesToPropagate) {
        this._currentValuesToPropagate = _currentValuesToPropagate;
    }

    public void set_oldPropagater(ValuePropagater _oldPropagater) {
        this._oldPropagater = _oldPropagater;
    }

    public ValuePropagater get_oldPropagater() {
        return _oldPropagater;
    }

    public void propagate(IValuePropagateable classToPropagate) {
        Class<?> clazz = classToPropagate.getClass();
        Field[] fields = clazz.getDeclaredFields();
        System.out.println("Start propagating to " + classToPropagate);
        for(Field field : fields){
            PropagateValue propagateValueAnnotation = field.getAnnotation(PropagateValue.class);
            if(propagateValueAnnotation != null){

                field.setAccessible(true);
                Object mainFieldobject = null;
                try {
                    mainFieldobject = field.get(classToPropagate);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                Boolean isElligibleForAggregation = false;

                if(propagateValueAnnotation.classToPropagate().isInstance(mainFieldobject)){
                    isElligibleForAggregation = true;
                } else if(mainFieldobject instanceof List) {
                    ListIterator listIterator = ((List) mainFieldobject).listIterator();
                    while (listIterator.hasNext()) {
                        Object value = listIterator.next();
                        if (propagateValueAnnotation.classToPropagate().isInstance(value)) {
                            isElligibleForAggregation = true;
                        }
                    }
                }

                if(isElligibleForAggregation){
                    //aggregate value
                    if(_oldPropagater != null){
                        if(mainFieldobject instanceof List){
                            //aggragation of list
                            _currentValuesToPropagate.addAll(_oldPropagater.get_currentValuesToPropagate());
                            _currentValuesToPropagate.addAll((List)mainFieldobject);
                            try {
                                field.set(classToPropagate, _currentValuesToPropagate);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        } else {

                        }
                    }
                }


                //check about the fields to propagate
                String[] fieldsNameToPropagate = propagateValueAnnotation.fieldsNameToPropagate();
                for(String fieldName : fieldsNameToPropagate){
                    try {
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


                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }

                }

            }
        }
    }
}
