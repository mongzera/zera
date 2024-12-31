# Zera Entity-Component System
Simple and Versatile Entity-Component System for Java Game Developers.

Zera is an ECS library for easy integration of entity-component architecture instead of the traditional OOP.
This library is made for Beginner Java Game Developers looking to speed up their development and 
avoid hassles when creating systems that come with OOP.

ECS architecture is like a database where all information about an object is stored.
We use an index or an ID to determine a particular object, then that object can have components.

If you are familiar with arrays, the way ECS works is that there is a ComponentPool which is basically an array of the same type of Component.
Now, an instance of a Component is stored in that array in a specific index exactly the same with the EntityID if and only if that EntityID is specified to have this particular property.

## Why Zera ECS

- Easy to integrate
- Easy to learn
- Rapid Development
- No further overheads
- Simple and Basic
- Versatile

### Entity
Entity is just an integer that is used to identify an object. Basically, an ID.\
To create an entity:
```
Entity e = engine.createEntity();
```

### Component
AComponent is an attribute or a property that an entity posses.
It is a characteristic that determines a behaviour or outcome of an Entity.
At its core, it is a modular piece of information about some entity.\
We define some AComponent named Position:
```
public class Position extends AComponent {
    public float x, y, z;
}
```

Then, we define another AComponent named Velocity

```
public class Velocity extends AComponent {
    public float vx, vy, vz;
}
```

### System
ASystem is a system that handles specific components.
This is where we make our logic responsible for the behavior of the entities.\
If a particular entity posses such properties, then we will create our logic responsible for how it will interact and behave in the world.

```
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

            //This is an example of a basic Numerical Integrator
            p.x += v.vx * deltaTime * 0.5f;
            p.y += v.vy * deltaTime * 0.5f;
            p.z += v.vz * deltaTime * 0.5f;

            //Handle Collisions

            p.x += v.vx * deltaTime * 0.5f;
            p.y += v.vy * deltaTime * 0.5f;
            p.z += v.vz * deltaTime * 0.5f;
        }
    }
}
```

In this example, we initialize a system by acquiring the ComponentPools we will process in the `init()`. In this case, `ComponentPool<Position>` and `ComponentPool<Velocity>`.

In the `updateSystem()` function, we define an `Archetype` which will be explained below. Then we will iterate the archetype and get the `entityId` and its corresponding `Position` and `Velocity` components.
After we have all the necessary information, we will perform our logic which is in this case a simple numerical integrator. Basically, we change an object's position depending on its velocity.
If you noticed, it is multiplied by `deltaTime` which is the elapsed time (milliseconds) from the current frame and the last frame. This is fundamental game development and you can learn more about it.

There's a lot of techniques out there, especially for Numerical Integration, this is an example of the Euler Method.

[DeltaTime](https://www.youtube.com/watch?v=yGhfUcPjXuE)\
[Numerical Integrator](https://www.youtube.com/watch?v=-GWTDhOQU6M)

