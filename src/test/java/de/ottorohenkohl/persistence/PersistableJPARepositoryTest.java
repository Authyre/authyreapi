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
    
    private final Identifier notStoredIdentifier;
    
    private final Identifier permittedIdentifier;
    
    private final PersistableRepository<T> repository;
    
    private final Positive amount;
    
    private final T notStoredPersistable;
    
    protected PersistableJPARepositoryTest(Identifier notStoredIdentifier, Identifier permittedIdentifier, PersistableRepository<T> repository, Positive amount, T notStoredPersistable) {
        this.notStoredIdentifier = notStoredIdentifier;
        this.permittedIdentifier = permittedIdentifier;
        this.repository = repository;
        this.amount = amount;
        this.notStoredPersistable = notStoredPersistable;
    }
    
    @Test
    @TestTransaction
    protected void createPersistableCaseNotExistingInDatabase() {
        repository.create(notStoredPersistable);
        
        assertTrue(repository.read(notStoredPersistable.getIdentifier()).isDefined());
    }
    
    @Test
    @TestTransaction
    protected void deleteFromDatabaseCaseExistingInDatabase() {
        var persistable = repository.read(permittedIdentifier).get();
        
        repository.delete(persistable);
        
        assertFalse(repository.read(permittedIdentifier).isDefined());
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
        var persistable = repository.read(permittedIdentifier);
        
        assertAll(() -> assertTrue(persistable.isDefined()),
                  () -> assertEquals(permittedIdentifier, persistable.get().getIdentifier()));
    }
    
    @Test
    protected void returnNoneOnReadCaseMissingInDatabase() {
        var persistable = repository.read(notStoredIdentifier);
        
        assertFalse(persistable.isDefined());
    }
    
    @Test
    @TestTransaction
    protected void updatePersistableCaseExistingInDatabase() {
        var persistable = repository.read(permittedIdentifier).get();
        
        assertAll(() -> assertEquals(Tag.STANDARD, persistable.getTag()),
                  () -> persistable.setTag(Tag.TEMPORARY),
                  () -> repository.update(persistable),
                  () -> assertEquals(Tag.TEMPORARY, persistable.getTag()));
    }
    
}
