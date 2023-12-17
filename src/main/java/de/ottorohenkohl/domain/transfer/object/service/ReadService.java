package de.ottorohenkohl.domain.transfer.object.service;

import de.ottorohenkohl.domain.model.entity.Service;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ReadService {
    
    String description;
    
    String identifier;
    
    String title;
    
    public ReadService(Service service) {
        this.description = service.getDescription() != null ? service.getDescription().getValue() : null;
        this.title = service.getTitle().getValue();
        this.identifier = service.getIdentifier().getUuid();
    }
    
}
