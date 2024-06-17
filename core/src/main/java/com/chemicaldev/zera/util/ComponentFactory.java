package com.chemicaldev.zera.util;

import com.chemicaldev.zera.ComponentPool;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class ComponentFactory <T>{

    public Class<T> type;

    public ComponentFactory(Class<T> type){
        this.type = type;
    }

    static public <T> T createComponent(Class<T> t){
        try {
            return t.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
