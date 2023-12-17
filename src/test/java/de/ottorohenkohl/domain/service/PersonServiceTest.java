package de.ottorohenkohl.domain.service;

import de.ottorohenkohl.domain.model.entity.Person;
import de.ottorohenkohl.domain.model.entity.PersonTest;
import de.ottorohenkohl.domain.model.enumeration.Status;
import de.ottorohenkohl.domain.model.enumeration.Trace;
import de.ottorohenkohl.domain.model.value.primitive.*;
import de.ottorohenkohl.domain.repository.PersonRepository;
import de.ottorohenkohl.domain.transfer.object.person.CreatePerson;
import de.ottorohenkohl.domain.transfer.object.person.UpdatePerson;
import io.quarkus.test.junit.QuarkusTest;
import io.vavr.control.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class PersonServiceTest {
    
    private PersonService service;
    
    private PersonRepository repository;
    
    @BeforeEach
    public void before() {
        this.repository = mock(PersonRepository.class);
        this.service = new PersonService(repository);
    }
    
    @Test
    protected void createPersonCaseNotExisting() {
        when(repository.read(any(Username.class))).thenReturn(Option.none());
        when(repository.create(any(Person.class))).thenReturn(Option.of(PersonTest.person));
        
        var createPerson = new CreatePerson(null, null, PasswordTest.notStoredValue, UsernameTest.notStoredValue);
        var response = service.create(createPerson);
        
        assertAll(() -> verify(repository).read(any(Username.class)),
                  () -> verify(repository).create(any(Person.class)),
                  () -> assertFalse(response.hasError()));
    }
    
    @Test
    protected void deletePersonByUsernameCaseExisting() {
        when(repository.read(any(Username.class))).thenReturn(Option.of(PersonTest.person));
        when(repository.delete(any(Person.class))).thenReturn(Option.of(PersonTest.person));
        
        var response = service.delete(UsernameTest.permittedValue);
        
        assertAll(() -> verify(repository).read(any(Username.class)),
                  () -> verify(repository).delete(any(Person.class)), () -> assertFalse(response.hasError()));
    }
    
    @Test
    protected void returnErrorOnCreateCaseUsernameExisting() {
        when(repository.read(any(Username.class))).thenReturn(Option.of(PersonTest.person));
        when(repository.create(any(Person.class))).thenReturn(Option.of(PersonTest.person));
        
        var createPerson = new CreatePerson(null, null, PasswordTest.permittedValue, UsernameTest.permittedValue);
        var response = service.create(createPerson);
        
        assertAll(() -> verify(repository).read(any(Username.class)),
                  () -> verifyNoMoreInteractions(repository),
                  () -> assertTrue(response.hasError()),
                  () -> assertEquals(Status.DUPLICATE, response.getError().getStatus()),
                  () -> assertEquals(Trace.USERNAME, response.getError().getTrace()));
    }
    
    @Test
    protected void returnErrorOnDeleteByUsernameCaseMissing() {
        when(repository.read(any(Username.class))).thenReturn(Option.none());
        when(repository.delete(any(Person.class))).thenReturn(Option.of(PersonTest.person));
        
        var response = service.delete(UsernameTest.permittedValue);
        
        assertAll(() -> verify(repository).read(any(Username.class)),
                  () -> verifyNoMoreInteractions(repository),
                  () -> assertTrue(response.hasError()),
                  () -> assertEquals(Status.MISSING, response.getError().getStatus()),
                  () -> assertEquals(Trace.DATABASE, response.getError().getTrace()));
    }
    
    @Test
    protected void returnErrorOnReadByUsernameCaseMissing() {
        when(repository.read(any(Username.class))).thenReturn(Option.none());
        
        var readPerson = service.read(UsernameTest.permittedValue);
        
        assertAll(() -> verify(repository).read(any(Username.class)),
                  () -> assertTrue(readPerson.hasError()),
                  () -> assertEquals(Status.MISSING, readPerson.getError().status),
                  () -> assertEquals(Trace.DATABASE, readPerson.getError().trace));
    }
    
    @Test
    protected void returnListOfReadPersonOnReadAll() {
        when(repository.readAll()).thenReturn(List.of(PersonTest.person));
        when(repository.readAmount()).thenReturn(1L);
        
        var pagination = service.readAll();
        
        assertAll(() -> verify(repository).readAll(),
                  () -> assertFalse(pagination.hasError()),
                  () -> assertEquals(1, pagination.getItems().size()),
                  () -> assertEquals(1, pagination.getNumberItems()),
                  () -> assertEquals(1, pagination.getNumberTotal()),
                  () -> assertEquals(PersonTest.person.getIdentifier().getUuid(),
                                     pagination.getItems().get(0).getIdentifier()));
    }
    
    @Test
    protected void returnListOfReadPersonWithPaginationOnReadAll() {
        when(repository.readAll()).thenReturn(List.of(PersonTest.person, PersonTest.person));
        when(repository.readAmount()).thenReturn(1L);
        
        var pagination = service.readAll(1);
        
        assertAll(() -> verify(repository).readAll(any(Positive.class)),
                  () -> assertFalse(pagination.hasError()),
                  () -> assertEquals(0, pagination.getItems().size()),
                  () -> assertEquals(0, pagination.getNumberItems()),
                  () -> assertEquals(1, pagination.getNumberTotal()));
    }
    
    @Test
    protected void returnErrorOnReadAllWithZeroPagination() {
        var pagination = service.readAll(0);
        
        assertAll(() -> assertTrue(pagination.hasError()),
                  () -> assertEquals(Status.FORMATTING, pagination.getError().getStatus()),
                  () -> assertEquals(Trace.POSITIVE, pagination.getError().getTrace()));
    }
    
    @Test
    protected void returnReadPersonOnReadByUsernameCaseExisting() {
        when(repository.read(any(Username.class))).thenReturn(Option.of(PersonTest.person));
        
        var readPerson = service.read(UsernameTest.permittedValue);
        
        assertAll(() -> verify(repository).read(any(Username.class)),
                  () -> assertFalse(readPerson.hasError()),
                  () -> assertEquals(PersonTest.person.getUsername().getValue(), readPerson.getItem().getUsername()),
                  () -> assertEquals(PersonTest.person.getIdentifier().getUuid(),
                                     readPerson.getItem().getIdentifier()));
    }
    
    @Test
    protected void updateForenameCaseExistingAndDifferent() {
        when(repository.read(any(Username.class))).thenReturn(Option.of(PersonTest.person));
        when(repository.update(any(Person.class))).thenReturn(Option.of(PersonTest.person));
        
        var args = ArgumentCaptor.forClass(Person.class);
        var updatePerson = new UpdatePerson(NameTest.notStoredValue, null, null);
        var response = service.update(PersonTest.person.getUsername().getValue(), updatePerson);
        
        assertAll(() -> assertFalse(response.hasError()),
                  () -> verify(repository).update(args.capture()),
                  () -> assertEquals(NameTest.notStoredValue, args.getValue().getForename().getValue()),
                  () -> assertEquals(PersonTest.person.getLastname(), args.getValue().getLastname()),
                  () -> assertEquals(PersonTest.person.getPassword(), args.getValue().getPassword()));
    }
    
    @Test
    protected void updateLastnameCaseExistingAndDifferent() {
        when(repository.read(any(Username.class))).thenReturn(Option.of(PersonTest.person));
        when(repository.update(any(Person.class))).thenReturn(Option.of(PersonTest.person));
        
        var args = ArgumentCaptor.forClass(Person.class);
        var updatePerson = new UpdatePerson(null, NameTest.notStoredValue, null);
        var response = service.update(PersonTest.person.getUsername().getValue(), updatePerson);
        
        assertAll(() -> assertFalse(response.hasError()),
                  () -> verify(repository).update(args.capture()),
                  () -> assertEquals(NameTest.notStoredValue, args.getValue().getLastname().getValue()),
                  () -> assertEquals(PersonTest.person.getForename(), args.getValue().getForename()),
                  () -> assertEquals(PersonTest.person.getPassword(), args.getValue().getPassword()));
    }
    
    @Test
    protected void updatePasswordCaseExistingAndDifferent() {
        when(repository.read(any(Username.class))).thenReturn(Option.of(PersonTest.person));
        when(repository.update(any(Person.class))).thenReturn(Option.of(PersonTest.person));
        
        var args = ArgumentCaptor.forClass(Person.class);
        var updatePerson = new UpdatePerson(null, null, PasswordTest.notStoredValue);
        var response = service.update(PersonTest.person.getUsername().getValue(), updatePerson);
        
        assertAll(() -> assertFalse(response.hasError()),
                  () -> verify(repository).update(args.capture()),
                  () -> assertEquals(PersonTest.person.getForename(), args.getValue().getForename()),
                  () -> assertEquals(PersonTest.person.getLastname(), args.getValue().getLastname()),
                  () -> assertEquals(Password.build(PasswordTest.notStoredValue).get(), args.getValue().getPassword()));
    }
    
}
