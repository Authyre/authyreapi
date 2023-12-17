package de.ottorohenkohl.domain.transfer.response;

import de.ottorohenkohl.domain.model.value.embedded.Error;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Answer {
    
    protected Error error;
    
    public boolean hasError() {
        return this.error != null;
    }
    
}