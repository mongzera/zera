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

    public boolean has(Bitmask mask){
        return has(mask.getBitset());
    }

    public int getBitset(){
        return mask;
    }

    public Bitmask or(Bitmask mask){
        this.mask |= mask.getBitset();
        return this;
    }

    public int popCount(){
        int count = 0;
        int tempMask = getBitset();

        while(tempMask != 1){
            tempMask >>= 1;
            count++;
        }

        return count;
    }

    @Override
    public String toString() {
        return Integer.toBinaryString(mask);
    }
}
