package com.chemicaldev.zera;

import com.chemicaldev.zera.util.Bitmask;

public class ComponentPool <T>{
    private Bitmask componentSignature;
    private int poolSize = 0;

    public Class<T> type;
    private T[] componentList;
    public ComponentPool(Class<T> type, int poolSize, Bitmask componentSignature){
        this.type = type;
        this.componentSignature = componentSignature;
        this.componentList = (T[]) new IComponent[poolSize];
        this.poolSize = poolSize;
    }

    public void setComponent(int index, T component){
        componentList[index] = component;
    }

    public T getComponent(int index){
        return this.componentList[index];
    }
}
