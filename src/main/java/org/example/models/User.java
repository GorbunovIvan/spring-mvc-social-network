package org.example.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "users")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = { "id", "name", "surname" })
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 99, message = "Name should be between 1 and 99 characters")
    private String name;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 99, message = "Surname should be between 1 and 99 characters")
    private String surname;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 30, message = "Login should be between 4 and 30 characters")
    private String login;

    @NotNull
    @NotBlank
    @Size(min = 6, max = 30, message = "Password should be between 6 and 30 characters")
    private String password;

    @Column(name = "birth_day")
    private LocalDate birthDay;

    @OneToMany(mappedBy = "author")
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "inviter")
    private Set<Friends> friendsIInvited = new HashSet<>();

    @OneToMany(mappedBy = "receiver")
    private Set<Friends> friendsWhoInvitedMe = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Message> messagesIWrote = new HashSet<>();

    @OneToMany(mappedBy = "receiver")
    private Set<Message> messagesIReceived = new HashSet<>();

    public String getFullName() {
        return name + " " + surname;
    }

    public Set<User> getFriends() {

        Stream<User> iInvited = friendsIInvited.stream()
                .map(Friends::getReceiver);

        Stream<User> whoInvitedMe = friendsWhoInvitedMe.stream()
                .map(Friends::getInviter);

        return Stream.concat(iInvited, whoInvitedMe).collect(Collectors.toSet());
    }

    public Set<Message> getMessages(Integer userId) {

        Stream<Message> iWrote = messagesIWrote.stream()
                .filter(m -> m.getReceiver().getId().equals(userId));

        Stream<Message> iReceived = messagesIReceived.stream()
                .filter(m -> m.getAuthor().getId().equals(userId));

        return Stream.concat(iWrote, iReceived).collect(Collectors.toSet());
    }
}
