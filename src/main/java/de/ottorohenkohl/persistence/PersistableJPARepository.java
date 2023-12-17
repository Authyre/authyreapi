package de.ottorohenkohl.persistence;

import de.ottorohenkohl.domain.model.entity.Persistable;
import de.ottorohenkohl.domain.model.entity.Persistable_;
import de.ottorohenkohl.domain.model.value.embedded.Identifier;
import de.ottorohenkohl.domain.model.value.primitive.Positive;
import de.ottorohenkohl.domain.repository.PersistableRepository;
import io.vavr.control.Option;
import io.vavr.control.Try;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class PersistableJPARepository<T extends Persistable> implements PersistableRepository<T> {
    
    @NonNull
    protected Class<T> persistable;
    
    @PersistenceContext
    protected EntityManager entityManager;
    
    @Override
    public List<T> readAll() {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(persistable);
        
        query.from(persistable);
        
        return entityManager.createQuery(query).getResultList();
    }
    
    @Override
    public List<T> readAll(Positive pages) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(persistable);
        
        query.from(persistable);
        
        return entityManager.createQuery(query).setMaxResults(pages.getValue()).getResultList();
    }
    
    @Override
    public Long readAmount() {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(Long.class);
        var root = query.from(persistable);
        
        query.select(builder.count(root.get(Persistable_.identifier)));
        
        return entityManager.createQuery(query).getSingleResult();
    }
    
    @Override
    @Transactional
    public Option<T> create(T persistable) {
        return Try.run(() -> entityManager.persist(persistable))
                  .map(t -> persistable)
                  .andFinally(entityManager::flush)
                  .toOption();
    }
    
    @Transactional
    public Option<T> delete(T persistable) {
        var removable = entityManager.merge(persistable);
        return Try.run(() -> entityManager.remove(removable))
                  .map(t -> persistable)
                  .andFinally(entityManager::flush)
                  .toOption();
    }
    
    @Override
    public Option<T> read(Identifier identifier) {
        return Option.of(entityManager.find(persistable, identifier));
    }
    
    @Override
    @Transactional
    public Option<T> update(T persistable) {
        return Try.of(() -> entityManager.merge(persistable)).andFinally(entityManager::flush).toOption();
    }
    
}

