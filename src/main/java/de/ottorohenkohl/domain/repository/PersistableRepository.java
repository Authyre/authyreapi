package de.ottorohenkohl.domain.repository;

import de.ottorohenkohl.domain.model.entity.Persistable;
import de.ottorohenkohl.domain.model.value.embedded.Identifier;
import de.ottorohenkohl.domain.model.value.primitive.Positive;
import io.vavr.control.Option;

import java.util.List;

public interface PersistableRepository<T extends Persistable> {
    
    List<T> readAll();
    
    List<T> readAll(Positive pages);
    
    Long readAmount();
    
    Option<T> create(T persistable);
    
    Option<T> delete(T persistable);
    
    Option<T> read(Identifier identifier);
    
    Option<T> update(T persistable);
    
}
