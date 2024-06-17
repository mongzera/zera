package com.chemicaldev.zera.system;

import com.chemicaldev.zera.ASystem;
import com.chemicaldev.zera.ComponentPool;
import com.chemicaldev.zera.components.Position;
import com.chemicaldev.zera.components.Velocity;

public class Physics extends ASystem {
    ComponentPool<Position> positionPool;
    ComponentPool<Velocity> velocityPool;
    @Override
    public void init() {
        positionPool = engine.getPool(Position.class);
        velocityPool = engine.getPool(Velocity.class);

    }

    @Override
    public void updateSystem() {
        for(int i = 0; i < engine.getCurrentEntitySize(); i++){
            Position pos = positionPool.getComponent(i);
            Velocity vel = velocityPool.getComponent(i);
            if(pos != null && vel != null){
                pos.x += vel.vx;
                pos.y += vel.vy;
                pos.z += vel.vz;
            }
        }
    }
}
