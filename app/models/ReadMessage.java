package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.utils.Model;

import javax.persistence.*;

@Entity
public class ReadMessage extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    public UserModel user;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    public MessageModel message;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public MessageStatus status = MessageStatus.UNREAD;

    public static Finder<Long, ReadMessage> find = new Finder<>(Long.class, ReadMessage.class);

    public enum MessageStatus {
        READ, UNREAD
    }

    public ReadMessage() {
    }

    public ReadMessage(UserModel user, MessageModel message) {
        this.user = user;
        this.message = message;
    }
}
