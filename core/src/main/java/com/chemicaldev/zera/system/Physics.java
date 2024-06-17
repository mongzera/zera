package com.chemicaldev.zera.system;

import com.chemicaldev.zera.AComponent;
import com.chemicaldev.zera.ASystem;
import com.chemicaldev.zera.Archetype;
import com.chemicaldev.zera.ComponentPool;
import com.chemicaldev.zera.components.Mesh;
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
        Archetype physicsArch = engine.getArchetype(Mesh.class, Position.class, Velocity.class);
        for(int i = 0; i < physicsArch.length; i++){
            int entityId = physicsArch.entities[i].getEntityId();
            Position p = positionPool.getComponent(entityId);
            Velocity v = velocityPool.getComponent(entityId);

            p.x += v.vx;
            p.y += v.vy;
            p.z += v.vz;
        }
    }
}
