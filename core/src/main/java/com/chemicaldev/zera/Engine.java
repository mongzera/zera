package com.chemicaldev.zera;

import com.chemicaldev.zera.util.Bitmask;
import com.chemicaldev.zera.util.ComponentFactory;

import java.util.ArrayList;

public class Engine {
    private int MAX_COMPONENTS = 32;
    private int MAX_ENTITIES = 32;

    private static int m_numberOfCurrentComponents = 0;
    private ComponentPool<?>[] m_componentPools = new ComponentPool<?>[MAX_COMPONENTS];

    private int m_numberOfCurrentEntities = 0;
    private Entity[] m_entities = new Entity[MAX_ENTITIES];

    private ArrayList<ASystem> m_systemsList;

    public Engine(){
        m_systemsList = new ArrayList<>();
    }

    public void addSystem(ASystem system){
        system.engine = this;
        system.init();
        this.m_systemsList.add(system);
    }

    public void updateSystems(){
        for(ASystem system : this.m_systemsList){
            system.updateSystem();
        }
    }

    public <T extends IComponent> int getId(Class<T> type){
        for(int i = 0; i < m_componentPools.length; i++){
            ComponentPool<?> pool = getPool(i);
            if(pool == null) continue;
            if(type.equals(pool.type)) return i;

        }
        return -1;
    }

    public <T> ComponentPool<T> getPool(Class<T> type){
        for(int i = 0; i < m_componentPools.length; i++){
            ComponentPool<?> pool = getPool(i);
            if(pool == null) continue;
            if(type.equals(pool.type)) return (ComponentPool<T>) pool;
        }

        return null;
    }

    public <T> ComponentPool<T> getPool(int index){
        return (ComponentPool<T>) m_componentPools[index];
    }

    public <T extends IComponent> int createComponent(Class<T> type){
        if(getId(type) == -1){
            Bitmask componentSig = new Bitmask();
            componentSig.setTrue(m_numberOfCurrentComponents);
            m_componentPools[m_numberOfCurrentComponents++] = new ComponentPool<T>(type, MAX_ENTITIES, componentSig);
            return 0;
        }

        return -1;
    }

    public <T extends IComponent> void assignComponent(Class<T> componentType, Entity e){

        assignComponent(ComponentFactory.createComponent(componentType), e);

    }

    public <T extends IComponent> void assignComponent(T component, Entity e){
        int componentId = getId(component.getClass());
        ComponentPool<T> pool = getPool(componentId);
        pool.setComponent(e.getEntityId(), component);
        e.getComponentMask().setTrue(componentId);
    }

    public <T extends IComponent> T getComponentOfEntity(Class<T> componentType, Entity e){
        //checks if entity has a component
        int id = getId(componentType);
        if(e.getComponentMask().has(id)){
            return (T) getPool(id).getComponent(e.getEntityId());
        }

        return null;
    }

    public Entity createEntity(){
        m_entities[m_numberOfCurrentEntities] = new Entity(m_numberOfCurrentEntities++);
        return m_entities[m_numberOfCurrentEntities-1];
    }

    public int getCurrentEntitySize(){
        return m_numberOfCurrentEntities;
    }
}
