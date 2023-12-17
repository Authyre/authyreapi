package de.ottorohenkohl.domain.transfer.object.token;

import de.ottorohenkohl.domain.model.entity.Token;
import de.ottorohenkohl.domain.transfer.object.permission.ReadPermission;
import de.ottorohenkohl.domain.transfer.object.person.ReadPerson;
import de.ottorohenkohl.domain.transfer.object.service.ReadService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ReadToken {
    
    List<ReadPermission> permissions;
    
    ReadPerson person;
    
    ReadService service;
    
    String identifier;
    
    public ReadToken(Token token) {
        this.identifier = token.getIdentifier().toString();
        
        this.permissions = token.getPermissions().stream().map(ReadPermission::new).toList();
        this.person = new ReadPerson(token.getPerson());
        this.service = new ReadService(token.getService());
    }
    
}
