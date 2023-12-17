package de.ottorohenkohl.domain.transfer.object.person;

import de.ottorohenkohl.domain.model.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UpdatePerson {
    
    String forename;
    
    String lastname;
    
    String password;
    
    
    public UpdatePerson(Person person) {
        this.forename = person.getForename() != null ? person.getForename().getValue() : null;
        this.lastname = person.getLastname() != null ? person.getLastname().getValue() : null;
        this.password = person.getPassword().getValue();
    }
    
}
