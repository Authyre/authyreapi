package de.ottorohenkohl.domain.model.entity;

import de.ottorohenkohl.domain.model.value.primitive.Name;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@Entity
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
public class Service extends Persistable {
    
    private Name description;
    
    @Column(unique = true)
    @NonNull
    private Name title;
    
}
