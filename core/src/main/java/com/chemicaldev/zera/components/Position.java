package com.chemicaldev.zera.components;

import com.chemicaldev.zera.AComponent;

public class Position extends AComponent {
    public float x, y, z;

    public String toString(){

        return String.format("X: %s, Y: %s, Z: %s", x, y, z);
    }
}
