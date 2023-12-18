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
    
    private static final Name forename = new NameTest().getAbsentInstance();
    
    private static final Name lastname = new NameTest().getAbsentInstance();
    
    private static final Person person = new PersonTest().getAbsentInstance();
    
    private static final String password = new PasswordTest().getStoredOriginal();
    
    private static final Username username = new UsernameTest().getAbsentInstance();
    
    
    private PersonService personService;
    
    private PersonRepository personRepository;
    
    @BeforeEach
    public void before() {
        this.personRepository = mock(PersonRepository.class);
        this.personService = new PersonService(personRepository);
    }
    
    @Test
    protected void createPersonCaseNotExisting() {
        when(personRepository.read(any(Username.class))).thenReturn(Option.none());
        when(personRepository.create(any(Person.class))).thenReturn(Option.of(person));
        
        var createPerson = new CreatePerson(null, null, password, username.getValue());
        var response = personService.create(createPerson);
        
        assertAll(() -> verify(personRepository).read(any(Username.class)),
                  () -> verify(personRepository).create(any(Person.class)),
                  () -> assertFalse(response.hasError()));
    }
    
    @Test
    protected void deletePersonByUsernameCaseExisting() {
        when(personRepository.read(any(Username.class))).thenReturn(Option.of(person));
        when(personRepository.delete(any(Person.class))).thenReturn(Option.of(person));
        
        var response = personService.delete(username.getValue());
        
        assertAll(() -> verify(personRepository).read(any(Username.class)),
                  () -> verify(personRepository).delete(any(Person.class)),
                  () -> assertFalse(response.hasError()));
    }
    
    @Test
    protected void returnErrorOnCreateCaseUsernameExisting() {
        when(personRepository.read(any(Username.class))).thenReturn(Option.of(person));
        when(personRepository.create(any(Person.class))).thenReturn(Option.of(person));
        
        var createPerson = new CreatePerson(null, null, password, username.getValue());
        var response = personService.create(createPerson);
        
        assertAll(() -> verify(personRepository).read(any(Username.class)),
                  () -> verifyNoMoreInteractions(personRepository),
                  () -> assertTrue(response.hasError()),
                  () -> assertEquals(Status.DUPLICATE, response.getError().getStatus()),
                  () -> assertEquals(Trace.USERNAME, response.getError().getTrace()));
    }
    
    @Test
    protected void returnErrorOnDeleteByUsernameCaseMissing() {
        when(personRepository.read(any(Username.class))).thenReturn(Option.none());
        when(personRepository.delete(any(Person.class))).thenReturn(Option.of(person));
        
        var response = personService.delete(username.getValue());
        
        assertAll(() -> verify(personRepository).read(any(Username.class)),
                  () -> verifyNoMoreInteractions(personRepository),
                  () -> assertTrue(response.hasError()),
                  () -> assertEquals(Status.MISSING, response.getError().getStatus()),
                  () -> assertEquals(Trace.DATABASE, response.getError().getTrace()));
    }
    
    @Test
    protected void returnErrorOnReadByUsernameCaseMissing() {
        when(personRepository.read(any(Username.class))).thenReturn(Option.none());
        
        var readPerson = personService.read(username.getValue());
        
        assertAll(() -> verify(personRepository).read(any(Username.class)),
                  () -> assertTrue(readPerson.hasError()),
                  () -> assertEquals(Status.MISSING, readPerson.getError().getStatus()),
                  () -> assertEquals(Trace.DATABASE, readPerson.getError().getTrace()));
    }
    
    @Test
    protected void returnListOfReadPersonOnReadAll() {
        when(personRepository.readAll()).thenReturn(List.of(person));
        when(personRepository.readAmount()).thenReturn(1L);
        
        var pagination = personService.readAll();
        
        assertAll(() -> verify(personRepository).readAll(),
                  () -> assertFalse(pagination.hasError()),
                  () -> assertEquals(1, pagination.getItems().size()),
                  () -> assertEquals(1, pagination.getNumberItems()),
                  () -> assertEquals(1, pagination.getNumberTotal()),
                  () -> assertEquals(person.getIdentifier().getUuid(),
                                     pagination.getItems().get(0).getIdentifier()));
    }
    
    @Test
    protected void returnListOfReadPersonWithPaginationOnReadAll() {
        when(personRepository.readAll()).thenReturn(List.of(person, person));
        when(personRepository.readAmount()).thenReturn(1L);
        
        var pagination = personService.readAll(1);
        
        assertAll(() -> verify(personRepository).readAll(any(Positive.class)),
                  () -> assertFalse(pagination.hasError()),
                  () -> assertEquals(0, pagination.getItems().size()),
                  () -> assertEquals(0, pagination.getNumberItems()),
                  () -> assertEquals(1, pagination.getNumberTotal()));
    }
    
    @Test
    protected void returnErrorOnReadAllWithZeroPagination() {
        var pagination = personService.readAll(0);
        
        assertAll(() -> assertTrue(pagination.hasError()),
                  () -> assertEquals(Status.FORMATTING, pagination.getError().getStatus()),
                  () -> assertEquals(Trace.POSITIVE, pagination.getError().getTrace()));
    }
    
    @Test
    protected void returnReadPersonOnReadByUsernameCaseExisting() {
        when(personRepository.read(any(Username.class))).thenReturn(Option.of(person));
        
        var readPerson = personService.read(username.getValue());
        
        assertAll(() -> verify(personRepository).read(any(Username.class)),
                  () -> assertFalse(readPerson.hasError()),
                  () -> assertEquals(person.getUsername().getValue(), readPerson.getItem().getUsername()),
                  () -> assertEquals(person.getIdentifier().getUuid(),
                                     readPerson.getItem().getIdentifier()));
    }
    
    @Test
    protected void updateForenameCaseExistingAndDifferent() {
        when(personRepository.read(any(Username.class))).thenReturn(Option.of(person));
        when(personRepository.update(any(Person.class))).thenReturn(Option.of(person));
        
        var args = ArgumentCaptor.forClass(Person.class);
        var updatePerson = new UpdatePerson(forename.getValue(), null, null);
        var response = personService.update(person.getUsername().getValue(), updatePerson);
        
        assertAll(() -> assertFalse(response.hasError()),
                  () -> verify(personRepository).update(args.capture()),
                  () -> assertEquals(forename, args.getValue().getForename()),
                  () -> assertEquals(person.getLastname(), args.getValue().getLastname()),
                  () -> assertEquals(person.getPassword(), args.getValue().getPassword()));
    }
    
    @Test
    protected void updateLastnameCaseExistingAndDifferent() {
        when(personRepository.read(any(Username.class))).thenReturn(Option.of(person));
        when(personRepository.update(any(Person.class))).thenReturn(Option.of(person));
        
        var args = ArgumentCaptor.forClass(Person.class);
        var updatePerson = new UpdatePerson(null, lastname.getValue(), null);
        var response = personService.update(person.getUsername().getValue(), updatePerson);
        
        assertAll(() -> assertFalse(response.hasError()),
                  () -> verify(personRepository).update(args.capture()),
                  () -> assertEquals(lastname, args.getValue().getLastname()),
                  () -> assertEquals(person.getForename(), args.getValue().getForename()),
                  () -> assertEquals(person.getPassword(), args.getValue().getPassword()));
    }
    
    @Test
    protected void updatePasswordCaseExistingAndDifferent() {
        when(personRepository.read(any(Username.class))).thenReturn(Option.of(person));
        when(personRepository.update(any(Person.class))).thenReturn(Option.of(person));
        
        var args = ArgumentCaptor.forClass(Person.class);
        var updatePerson = new UpdatePerson(null, null, password);
        var response = personService.update(person.getUsername().getValue(), updatePerson);
        
        assertAll(() -> assertFalse(response.hasError()),
                  () -> verify(personRepository).update(args.capture()),
                  () -> assertEquals(person.getForename(), args.getValue().getForename()),
                  () -> assertEquals(person.getLastname(), args.getValue().getLastname()),
                  () -> assertEquals(new PasswordTest().getStoredInstance(), args.getValue().getPassword()));
    }
    
}
