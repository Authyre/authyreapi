package de.ottorohenkohl.domain.transfer.object.service;

import de.ottorohenkohl.domain.model.entity.Service;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class ReadService {
    
    String description;
    
    String identifier;
    
    String title;
    
    public ReadService(Service service) {
        this.description = service.getDescription().getValue();
        this.title = service.getTitle().getValue();
        this.identifier = service.getIdentifier().getUuid();
    }
    
}
