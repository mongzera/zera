package com.chemicaldev.zera.components;

import com.chemicaldev.zera.IComponent;

public class Position implements IComponent {
    public float x, y, z;

    public String toString(){

        return String.format("X: %s, Y: %s, Z: %s", x, y, z);
    }
}
