package de.ottorohenkohl.domain.model.entity;

import de.ottorohenkohl.domain.model.enumeration.Access;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;

@AllArgsConstructor
@Entity
@Getter
@NoArgsConstructor
@Setter
public class Permission extends Persistable {
    
    @Enumerated
    @NonNull
    private Access access;
    
    @ManyToOne
    @NonNull
    private Person person;
    
    @ManyToOne
    @NonNull
    private Service service;
    
}
