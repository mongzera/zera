package com.chemicaldev.zera;

import com.chemicaldev.zera.util.Bitmask;

public class ComponentPool <T>{
    private int poolSize = 0;

    public Class<T> type;
    private T[] componentList;
    public ComponentPool(Class<T> type, int poolSize){
        this.type = type;
        this.componentList = (T[]) new AComponent[poolSize];
        this.poolSize = poolSize;
    }

    public void setComponent(int index, T component){
        componentList[index] = component;
    }

    public T getComponent(int index){
        return this.componentList[index];
    }
}
