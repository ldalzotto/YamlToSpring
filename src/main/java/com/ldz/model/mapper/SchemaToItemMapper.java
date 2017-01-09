package com.ldz.model.mapper;

import com.ldz.model.Item;
import com.ldz.model.Schema;
import com.ldz.model.generic.IMapper;

/**
 * Created by loicd on 09/01/2017.
 */
public class SchemaToItemMapper implements IMapper<Schema, Item>{

    private static SchemaToItemMapper _instance = null;

    public static SchemaToItemMapper getInstance(){
        if(_instance == null){
            _instance = new SchemaToItemMapper();
        }
        return _instance;
    }

    private SchemaToItemMapper(){

    }

    public Item apply(Schema input) {

        Item returnItem = new Item();
        returnItem.set$ref(input.get$ref());
        returnItem.setCollectionFormat(null);
        returnItem.setExclusiveMaximum(input.getExclusiveMaximum());
        returnItem.setExclusiveMinimum(input.getExclusiveMinimum());
        returnItem.setFormat(input.getFormat());
        returnItem.setItems(input.getItems());
        returnItem.setMaximum(input.getMaximum());
        returnItem.setMaxItems(input.getMaxItems());
        returnItem.setMaxLength(input.getMaxLength());
        returnItem.setMinimum(input.getMinimum());
        returnItem.setMinItems(input.getMinItems());
        returnItem.setMinLength(input.getMinLength());
        returnItem.setPattern(input.getPattern());
        returnItem.setMultipleOf(input.getMultipleOf());
        returnItem.setType(input.getType());
        returnItem.setUniqueItems(null);

        returnItem.setAllOf(input.getAllOf());
        returnItem.setAdditionalProperties(input.getAdditionalProperties());
        returnItem.setProperties(input.getProperties());
        returnItem.set_enum(input.get_enum());
        returnItem.setDescription(input.getDescription());
        returnItem.setDiscriminator(input.getDiscriminator());
        returnItem.setExample(input.getExample());
        returnItem.setExternalDocs(input.getExternalDocs());
        returnItem.setReadOnly(input.getReadOnly());
        returnItem.setRequired(input.getRequired());
        returnItem.setXml(input.getXml());
        returnItem.setTitle(input.getTitle());

        return returnItem;
    }

}
