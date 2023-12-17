package de.ottorohenkohl.domain.model.value.primitive;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UsernameConverter extends PrimitiveConverter<Username, String> {
    
    public UsernameConverter() {
        super(Username::new);
    }
    
}
