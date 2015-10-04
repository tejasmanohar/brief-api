package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.utils.Model;

import javax.persistence.*;

@Entity
public class MessageModel extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 256, nullable = false)
    public String message;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    public UserModel owner;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    public ConversationModel conversation;

    public static Finder<Long, MessageModel> find = new Finder<>(Long.class, MessageModel.class);

    public MessageModel() {
    }

    public MessageModel(UserModel owner, ConversationModel conversation, String message) {
        this.owner = owner;
        this.message = message;
        this.conversation = conversation;
    }
}
