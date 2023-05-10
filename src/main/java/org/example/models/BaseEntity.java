package org.example.models;

import org.springframework.stereotype.Component;

@Component
public class BaseEntity {

    public Number getId() {
        throw new IllegalStateException("Entity has no id");
    }

    public void setId(Number id) {
        throw new IllegalStateException("Entity has no id");
    }
}
