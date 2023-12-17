package de.ottorohenkohl.domain.model.entity;

import de.ottorohenkohl.domain.model.value.primitive.Secret;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
public class Token extends Persistable {
    
    @JoinColumn
    @ManyToMany
    @NonNull
    private List<Permission> permissions;
    
    @ManyToOne
    @NonNull
    private Person person;
    
    @Column(unique = true)
    @NonNull
    private Secret secret;
    
    @ManyToOne
    @NonNull
    private Service service;
    
}
