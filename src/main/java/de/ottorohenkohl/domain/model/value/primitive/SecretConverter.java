package de.ottorohenkohl.domain.model.value.primitive;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SecretConverter extends PrimitiveConverter<Secret, String> {
    
    public SecretConverter() {
        super(Secret::new);
    }
    
}
