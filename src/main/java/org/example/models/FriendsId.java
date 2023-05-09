package org.example.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class FriendsId implements Serializable {
    private User inviter;
    private User receiver;
}
