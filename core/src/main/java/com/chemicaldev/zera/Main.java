package com.chemicaldev.zera;

import com.chemicaldev.zera.components.Mesh;
import com.chemicaldev.zera.components.Position;
import com.chemicaldev.zera.components.Velocity;
import com.chemicaldev.zera.system.Physics;
import com.chemicaldev.zera.util.Bitmask;

public class Main {
    static Engine engine;
    public Main(){
        engine = new Engine(10000, 32);

        engine.createComponent(Position.class);
        engine.createComponent(Velocity.class);
        engine.createComponent(Mesh.class);

        Entity e = engine.createEntity();
        Entity e2 = engine.createEntity();

        Velocity v = new Velocity();
        v.vx = 16f / 1000f;

        engine.assignComponent(Position.class, e);
        engine.assignComponent(Position.class, e2);
        engine.assignComponent(v, e);
        engine.assignComponent(v, e2);
        engine.assignComponent(Mesh.class, e2);

        engine.addSystem(new Physics());

        while(true){
            try {
                Thread.sleep(16);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            System.out.println(engine.getComponentOfEntity(Position.class, e));

            engine.updateSystems();
        }
    }

    public static void main(String[] args){
        new Main();
        Bitmask mask = new Bitmask();

        mask.setTrue(4);

        System.out.println(mask.popCount());
    }

}