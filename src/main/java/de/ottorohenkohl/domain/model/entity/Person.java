package de.ottorohenkohl.domain.model.entity;

import de.ottorohenkohl.domain.model.value.primitive.Name;
import de.ottorohenkohl.domain.model.value.primitive.Password;
import de.ottorohenkohl.domain.model.value.primitive.Username;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@Entity
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
public class Person extends Persistable {
    
    private Name forename;
    
    private Name lastname;
    
    @NonNull
    private Password password;
    
    @Column(unique = true)
    @NonNull
    private Username username;
    
}
