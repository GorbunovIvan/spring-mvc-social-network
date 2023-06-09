package org.example.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id", callSuper = false)
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull
    private User author;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @NotNull
    private User receiver;

    @Column(length = 999)
    @NotNull
    @Size(min = 1, message = "Text is empty")
    private String text;

    private LocalDateTime time;

    public Message(User author, User receiver) {
        this.author = author;
        this.receiver = receiver;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", author=" + author.getId() + ", " + author.getFullName() +
                ", receiver=" + receiver.getId() + ", " + receiver.getFullName() +
                ", text='" + text + '\'' +
                ", time=" + time +
                '}';
    }
}
