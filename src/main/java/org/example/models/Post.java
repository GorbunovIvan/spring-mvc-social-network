package org.example.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id", callSuper = false)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull(message = "Author should not be empty")
    private User author;

    @NotNull(message = "Content should not be empty")
    @Size(min = 1, max = 999, message = "Content should be between 1 and 999 characters")
    private String content;

    private LocalDateTime time;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", author-id=" + author.getId() +
                ", time=" + time +
                '}';
    }
}
