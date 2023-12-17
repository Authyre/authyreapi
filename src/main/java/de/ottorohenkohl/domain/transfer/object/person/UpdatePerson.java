package de.ottorohenkohl.domain.transfer.object.person;

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
    
}
