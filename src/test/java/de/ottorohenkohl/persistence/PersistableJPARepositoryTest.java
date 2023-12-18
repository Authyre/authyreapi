package de.ottorohenkohl.persistence;

import de.ottorohenkohl.domain.model.entity.Persistable;
import de.ottorohenkohl.domain.model.enumeration.Tag;
import de.ottorohenkohl.domain.model.value.embedded.Identifier;
import de.ottorohenkohl.domain.model.value.primitive.Positive;
import de.ottorohenkohl.domain.repository.PersistableRepository;
import io.quarkus.test.TestTransaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class PersistableJPARepositoryTest<T extends Persistable> {
    
    protected final Identifier absentIdentifier;
    
    protected final Identifier storedIdentifier;
    
    private final PersistableRepository<T> repository;
    
    private final Positive amount;
    
    protected final T absentPersistable;
    
    protected PersistableJPARepositoryTest(Identifier absentIdentifier, Identifier storedIdentifier, PersistableRepository<T> repository, Positive amount, T absentPersistable) {
        this.absentIdentifier = absentIdentifier;
        this.storedIdentifier = storedIdentifier;
        this.repository = repository;
        this.amount = amount;
        this.absentPersistable = absentPersistable;
    }
    
    @Test
    @TestTransaction
    protected void createPersistableCaseNotExistingInDatabase() {
        repository.create(absentPersistable);
        
        assertTrue(repository.read(absentPersistable.getIdentifier()).isDefined());
    }
    
    @Test
    @TestTransaction
    protected void deleteFromDatabaseCaseExistingInDatabase() {
        var persistable = repository.read(storedIdentifier).get();
        
        repository.delete(persistable);
        
        assertFalse(repository.read(storedIdentifier).isDefined());
    }
    
    @Test
    protected void returnAmountOnReadAmount() {
        var persistable = repository.readAmount();
        
        assertEquals((long) amount.getValue(), persistable);
    }
    
    @Test
    protected void returnListOfPersistableOnReadAll() {
        var persistable = repository.readAll();
        
        assertEquals(amount.getValue(), persistable.size());
    }
    
    @Test
    protected void returnListOfPersistableWithPaginationOnReadAll() {
        var pages = Positive.build(amount.getValue() - 1);
        var persistable = repository.readAll(pages.get());
        
        assertEquals(pages.get().getValue(), persistable.size());
    }
    
    @Test
    protected void returnPersistableOnReadCaseExistingInDatabase() {
        var persistable = repository.read(storedIdentifier);
        
        assertAll(() -> assertTrue(persistable.isDefined()),
                  () -> assertEquals(storedIdentifier, persistable.get().getIdentifier()));
    }
    
    @Test
    protected void returnNoneOnReadCaseMissingInDatabase() {
        var persistable = repository.read(absentIdentifier);
        
        assertFalse(persistable.isDefined());
    }
    
    @Test
    @TestTransaction
    protected void updatePersistableCaseExistingInDatabase() {
        var persistable = repository.read(storedIdentifier).get();
        
        assertAll(() -> assertEquals(Tag.STANDARD, persistable.getTag()),
                  () -> persistable.setTag(Tag.TEMPORARY),
                  () -> repository.update(persistable),
                  () -> assertEquals(Tag.TEMPORARY, persistable.getTag()));
    }
    
}
