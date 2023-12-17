package de.ottorohenkohl.domain.transfer.response;

import de.ottorohenkohl.domain.model.value.embedded.Error;
import lombok.Getter;

@Getter
public class Empty extends Answer {
    
    public Empty() {
        this.error = null;
    }
    
    public Empty(Error error) {
        this.error = error;
    }
    
}
