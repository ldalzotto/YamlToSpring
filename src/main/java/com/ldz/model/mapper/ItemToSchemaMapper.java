package com.ldz.model.mapper;

import com.ldz.model.Item;
import com.ldz.model.Schema;
import com.ldz.model.generic.IMapper;

/**
 * Created by loicd on 09/01/2017.
 */
public class ItemToSchemaMapper implements IMapper<Item, Schema>{

    private static ItemToSchemaMapper _instance = null;

    public static ItemToSchemaMapper getInstance(){
        if(_instance == null){
            _instance = new ItemToSchemaMapper();
        }
        return _instance;
    }

    private ItemToSchemaMapper(){

    }

    public Schema apply(Item input) {
        Schema returnSchema = new Schema();

        returnSchema.set$ref(input.get$ref());
        returnSchema.setExclusiveMaximum(input.getExclusiveMaximum());
        returnSchema.setExclusiveMinimum(input.getExclusiveMinimum());
        returnSchema.setFormat(input.getFormat());
        returnSchema.setItems(input.getItems());
        returnSchema.setMaximum(input.getMaximum());
        returnSchema.setMaxItems(input.getMaxItems());
        returnSchema.setMaxLength(input.getMaxLength());
        returnSchema.setMinimum(input.getMinimum());
        returnSchema.setMinItems(input.getMinItems());
        returnSchema.setMinLength(input.getMinLength());
        returnSchema.setPattern(input.getPattern());
        returnSchema.setMultipleOf(input.getMultipleOf());
        returnSchema.setType(input.getType());

        return returnSchema;
    }

}
