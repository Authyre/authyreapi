package de.ottorohenkohl.domain.transfer.object.permission;

import de.ottorohenkohl.domain.model.entity.Permission;
import de.ottorohenkohl.domain.transfer.object.person.ReadPerson;
import de.ottorohenkohl.domain.transfer.object.service.ReadService;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class ReadPermission {
    
    ReadPerson person;
    
    ReadService service;
    
    String access;
    
    String identifier;
    
    String scope;
    
    public ReadPermission(Permission permission) {
        this.access = permission.getAccess().toString();
        this.scope = permission.getScope().toString();
        this.identifier = permission.getIdentifier().getUuid();
        
        this.person = new ReadPerson(permission.getPerson());
        this.service = new ReadService(permission.getService());
    }
    
}
