package com.ezbuy.framework.factory;

import org.modelmapper.ModelMapper;

public class ModelMapperFactory {
    private static ModelMapper modelMapper = new ModelMapper();

    public static ModelMapper getInstance() {
        return modelMapper;
    }

}
