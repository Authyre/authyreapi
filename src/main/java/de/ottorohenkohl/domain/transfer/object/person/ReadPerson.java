package de.ottorohenkohl.domain.transfer.object.person;

import de.ottorohenkohl.domain.model.entity.Person;
import lombok.*;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ReadPerson {
    
    String forename;
    
    String identifier;
    
    String lastname;
    
    String username;
    
    public ReadPerson(Person person) {
        this.forename = person.getForename() != null ? person.getForename().getValue() : null;
        this.identifier = person.getIdentifier().getUuid();
        this.lastname = person.getLastname() != null ? person.getLastname().getValue() : null;
        this.username = person.getUsername().getValue();
    }
    
}
