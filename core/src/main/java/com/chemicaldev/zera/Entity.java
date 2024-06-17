package com.chemicaldev.zera;

import com.chemicaldev.zera.util.Bitmask;

public class Entity {
    private int entityId;
    private Bitmask componentMask;

    public Entity(int entityId){
        this.entityId = entityId;
        this.componentMask = new Bitmask();
    }

    public int getEntityId() {
        return entityId;
    }

    public Bitmask getComponentMask() {
        return componentMask;
    }
}
