package de.ottorohenkohl.domain.model.entity;

public abstract class PersistableTest<T extends Persistable> {
    
    public abstract T getAbsentInstance();
    
}
