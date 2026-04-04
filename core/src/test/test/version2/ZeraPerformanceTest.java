package test.version2;

import com.chemicaldev.zera.*;

public class ZeraPerformanceTest {

    // 1. Define Components (POJOs)
    public static class Position extends AComponent { public float x, y; }
    public static class Velocity extends AComponent { public float dx = 1.0f, dy = 1.0f; }

    // 2. Define a System
    public static class MovementSystem extends ASystem {
        @Override public void init() {}
        @Override
        public void updateSystem() {
            engine.forEach(entity -> {
                Position p = engine.getComponentOfEntity(Position.class, entity);
                Velocity v = engine.getComponentOfEntity(Velocity.class, entity);
                p.x += v.dx;
                p.y += v.dy;
            }, Position.class, Velocity.class);
        }
    }

    public static void main(String[] args) {
        // Initialize for 20k entities and 32 components
        Engine engine = new Engine(20000, 32);

        // Register components
        engine.createComponent(Position.class);
        engine.createComponent(Velocity.class);

        // Add System
        engine.addSystem(new MovementSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new MovementSystem());

        Entity middleEntity = null;

        // 3. Create 20,000 Entities
        for (int i = 0; i < 19999; i++) {
            Entity e = engine.createEntity();
            engine.assignComponent(new Position(), e);
            engine.assignComponent(new Velocity(), e);

            if(i == 500) middleEntity = e;
        }

        int iter = 0;

        while(iter < 1_000_000){
            // 4. Run Update Loop
            long startTime = System.nanoTime();
            engine.updateSystems();
            long endTime = System.nanoTime();

            System.out.println("Update for 20k entities took: " + (endTime - startTime) / 1_000_000.0 + "ms");
            iter++;
        }

        assert middleEntity != null;
        engine.removeEntity(middleEntity);

        Entity recycled = engine.createEntity(); // Should reuse the same ID
        System.out.println("Recycled ID: " + recycled.getEntityId());
    }
}