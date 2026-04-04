package com.chemicaldev.zera;

import java.util.*;
import java.util.function.Consumer;

public class Engine {
    private final int MAX_COMPONENTS;
    private final int MAX_ENTITIES;

    private int m_numberOfCurrentComponents = 0;
    private final ComponentPool<?>[] m_componentPools;

    private int m_numberOfCurrentEntities = 0;
    private final Entity[] m_entities;

    // Track freed IDs for re-use
    private final Stack<Integer> m_freeSlots = new Stack<>();

    private final List<ASystem> m_systemsList = new ArrayList<>();
    private final Map<Class<?>, Integer> m_typeToId = new HashMap<>();
    private final Map<Class<?>, ComponentPool<?>> m_poolByType = new HashMap<>();

    public Engine(int maxEntity, int maxComponent) {
        this.MAX_ENTITIES = maxEntity;
        this.MAX_COMPONENTS = maxComponent;
        this.m_componentPools = new ComponentPool<?>[MAX_COMPONENTS];
        this.m_entities = new Entity[MAX_ENTITIES];
    }

    /**
     * Executes logic for every entity that matches the required component types.
     * Zero-allocation: No new arrays or lists are created during this call.
     */
    public void forEach(Consumer<Entity> action, Class<?>... componentTypes) {
        int targetMask = 0;
        for (Class<?> type : componentTypes) {
            Integer id = m_typeToId.get(type);
            if (id != null) targetMask |= (1 << id);
        }

        // Iterate up to the highest allocated ID
        for (int i = 0; i < m_numberOfCurrentEntities; i++) {
            Entity entity = m_entities[i];
            if (entity != null && entity.getComponentMask().has(targetMask)) {
                action.accept(entity);
            }
        }
    }

    public Entity createEntity() {
        int id;
        if (!m_freeSlots.isEmpty()) {
            // Reuse a previously deleted slot
            id = m_freeSlots.pop();
        } else {
            // Take the next available high-water mark slot
            if (m_numberOfCurrentEntities >= MAX_ENTITIES) {
                throw new RuntimeException("Maximum entity limit reached.");
            }
            id = m_numberOfCurrentEntities++;
        }

        Entity entity = new Entity(id);
        m_entities[id] = entity;
        return entity;
    }

    public void removeEntity(Entity e) {
        int entityId = e.getEntityId();
        if (m_entities[entityId] == null) return;

        for (int i = 0; i < m_numberOfCurrentComponents; i++) {
            m_componentPools[i].setComponent(entityId, null);
        }

        // Reset the mask entirely for the next user of this ID
        // We don't need to loop setFalse, just clear the mask object.
        e.getComponentMask().setAllFalse(); // Suggest adding a clear() or setFalseAll() to Bitmask

        m_entities[entityId] = null;
        m_freeSlots.push(entityId);
    }

    public <T extends AComponent> int createComponent(Class<T> type) {
        if (getId(type) == -1) {
            int id = m_numberOfCurrentComponents++;
            m_typeToId.put(type, id);
            ComponentPool<T> pool = new ComponentPool<>(type, MAX_ENTITIES);
            m_componentPools[id] = pool;
            m_poolByType.put(type, pool);
            return 0;
        }
        return -1;
    }

    public <T extends AComponent> int getId(Class<T> type) {
        Integer id = m_typeToId.get(type);
        return (id != null) ? id : -1;
    }

    public <T> ComponentPool<T> getPool(Class<T> type) {
        return (ComponentPool<T>) m_poolByType.get(type);
    }

    public <T extends AComponent> void assignComponent(T component, Entity e) {
        int id = getId(component.getClass());

        if (id != -1) {
            // Use the ID to index into the pool array directly
            ComponentPool<T> pool = (ComponentPool<T>) m_componentPools[id];
            pool.setComponent(e.getEntityId(), component);

            // Bitmask.setTrue(id) internally does (1 << id)
            // DO NOT shift it here, or you will shift a shifted bit.
            e.getComponentMask().setTrue(id);
        }
    }

    public <T extends AComponent> T getComponentOfEntity(Class<T> componentType, Entity e) {
        int id = getId(componentType);

        // Safety check: ID must exist
        if (id == -1) return null;

        // We must pass the BITSET (1 << id) to the has() method
        // because Bitmask.has(int bitset) checks: (mask & bitset) == bitset
        int bitset = (1 << id);

        if (e.getComponentMask().has(bitset)) {
            return (T) m_componentPools[id].getComponent(e.getEntityId());
        }

        return null;
    }

    public void addSystem(ASystem system) {
        system.engine = this;
        system.init();
        m_systemsList.add(system);
    }

    public void updateSystems() {
        for (ASystem system : m_systemsList) {
            system.updateSystem();
        }
    }
}