package de.ottorohenkohl.domain.transfer.object.service;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class CreateService {
    
    String description;
    
    String title;
    
}
