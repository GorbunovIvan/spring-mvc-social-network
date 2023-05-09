package org.example.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@EqualsAndHashCode
public class FriendsId implements Serializable {
    private User inviter;
    private User receiver;
}
