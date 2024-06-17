package com.chemicaldev.zera.util;

public class Bitmask {
    private int mask = 0b0;

    public Bitmask(int mask){
        this.mask = mask;
    }

    public Bitmask(){
        this.mask = 0;
    }

    public void setTrue(int bitPlacement){
        mask |= (1 << bitPlacement);
    }

    public void setFalse(int bitPlacement){
        mask &= ~(1 << bitPlacement);
    }

    public boolean has(int bitset){
        return (mask & bitset) == bitset;
    }

    @Override
    public String toString() {
        return Integer.toBinaryString(mask);
    }
}
