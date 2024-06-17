package com.chemicaldev.zera;

import com.chemicaldev.zera.components.Position;
import com.chemicaldev.zera.components.Velocity;
import com.chemicaldev.zera.system.Physics;

public class Main {
    static Engine engine;
    public Main(){
        engine = new Engine();

        engine.createComponent(Position.class);
        engine.createComponent(Velocity.class);

        Entity e = engine.createEntity();

        Velocity v = new Velocity();
        v.vx = 16f / 1000f;

        engine.assignComponent(Position.class, e);
        engine.assignComponent(v, e);

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

}