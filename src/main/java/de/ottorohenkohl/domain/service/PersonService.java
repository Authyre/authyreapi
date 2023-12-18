package de.ottorohenkohl.domain.service;

import de.ottorohenkohl.domain.model.entity.Person;
import de.ottorohenkohl.domain.model.enumeration.Status;
import de.ottorohenkohl.domain.model.enumeration.Trace;
import de.ottorohenkohl.domain.model.value.embedded.Error;
import de.ottorohenkohl.domain.model.value.primitive.*;
import de.ottorohenkohl.domain.repository.PersonRepository;
import de.ottorohenkohl.domain.transfer.object.person.CreatePerson;
import de.ottorohenkohl.domain.transfer.object.person.ReadPerson;
import de.ottorohenkohl.domain.transfer.object.person.UpdatePerson;
import de.ottorohenkohl.domain.transfer.response.Empty;
import de.ottorohenkohl.domain.transfer.response.Fetched;
import de.ottorohenkohl.domain.transfer.response.Pagination;
import io.vavr.collection.Traversable;
import io.vavr.control.Option;
import io.vavr.control.Validation;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Dependent
public class PersonService {
    
    private final PersonRepository personRepository;
    
    @Inject
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    
    private static Validation<Error, Person> insertForenameOrLeaveNull(Person person, String forename) {
        return Option.of(forename)
                     .toEither(Validation.<Error, Person> valid(person))
                     .map(Name::build)
                     .map(u -> u.peek(person::setForename).map(v -> person))
                     .fold(u -> u, u -> u);
    }
    
    private static Validation<Error, Person> insertLastnameOrLeaveNull(Person person, String lastname) {
        return Option.of(lastname)
                     .toEither(Validation.<Error, Person> valid(person))
                     .map(Name::build)
                     .map(u -> u.peek(person::setLastname).map(v -> person))
                     .fold(u -> u, u -> u);
    }
    
    public Empty create(CreatePerson createPerson) {
        return Validation.combine(Password.build(createPerson.getPassword()),
                                  Username.build(createPerson.getUsername()))
                         .ap(Person::new)
                         .mapError(Traversable::head)
                         .filter(t -> personRepository.read(t.getUsername()).isEmpty())
                         .toValidation(new Error(Status.DUPLICATE, Trace.USERNAME))
                         .flatMap(t -> t)
                         .map(t -> insertForenameOrLeaveNull(t, createPerson.getForename()))
                         .flatMap(t -> t)
                         .map(t -> insertLastnameOrLeaveNull(t, createPerson.getLastname()))
                         .flatMap(t -> t)
                         .peek(personRepository::create)
                         .fold(Empty::new, t -> new Empty());
    }
    
    public Empty delete(String username) {
        return Username.build(username)
                       .map(personRepository::read)
                       .flatMap(t -> t.toValidation(new Error(Status.MISSING, Trace.DATABASE)))
                       .map(personRepository::delete)
                       .flatMap(t -> t.toValidation(new Error(Status.INTERNAL, Trace.DATABASE)))
                       .fold(Empty::new, t -> new Empty());
    }
    
    @Transactional
    public Empty update(String username, UpdatePerson updatePerson) {
        return Username.build(username)
                       .map(personRepository::read)
                       .flatMap(t -> t.toValidation(new Error(Status.MISSING, Trace.DATABASE)))
                       .map(t -> update(t, updatePerson))
                       .fold(Empty::new, u -> u);
        
    }
    
    public Fetched<ReadPerson> read(String username) {
        return Username.build(username)
                       .map(personRepository::read)
                       .flatMap(t -> t.toValidation(new Error(Status.MISSING, Trace.DATABASE)))
                       .map(ReadPerson::new)
                       .fold(Fetched::new, Fetched::new);
    }
    
    public Pagination<ReadPerson> readAll() {
        return new Pagination<>(personRepository.readAmount(),
                                personRepository.readAll().stream().map(ReadPerson::new).toList());
    }
    
    public Pagination<ReadPerson> readAll(Integer pages) {
        return Positive.build(pages)
                       .fold(Pagination::new,
                             t -> new Pagination<>(personRepository.readAmount(),
                                                   personRepository.readAll(t).stream().map(ReadPerson::new).toList()));
    }
    
    private Empty update(Person person, UpdatePerson updatePerson) {
        return Validation.combine(Primitive.update(Name::build, person::getForename, updatePerson.getForename()),
                                  Primitive.update(Name::build, person::getLastname, updatePerson.getLastname()),
                                  Primitive.update(Password::build, person::getPassword, updatePerson.getPassword()))
                         .ap((forename, lastname, password) -> {
                             person.setForename(forename);
                             person.setLastname(lastname);
                             person.setPassword(password);
                             
                             personRepository.update(person);
                             
                             return new Empty();
                         })
                         .mapError(Traversable::head)
                         .fold(Empty::new, u -> u);
    }
    
}
