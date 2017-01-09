package com.ldz.model.generic;

/**
 * Created by loicd on 09/01/2017.
 */
public interface IMapper<I,O> {
    public O apply(I input);
}
