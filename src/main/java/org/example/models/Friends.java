package org.example.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@IdClass(FriendsId.class)
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = { "inviter", "receiver" }, callSuper = false)
public class Friends extends BaseEntity {

    @Id
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "inviter_id")
    private User inviter;

    @Id
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private LocalDateTime time;

    @Override
    public String toString() {
        return "Friends{" +
                "inviter=" + inviter.getId() + ", " + inviter.getFullName() +
                ", receiver=" + receiver.getId() + ", " + receiver.getFullName() +
                ", time=" + time +
                '}';
    }
}
